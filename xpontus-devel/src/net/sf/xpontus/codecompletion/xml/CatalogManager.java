/*
 * CatalogManager.java
 *
 * Created on January 25, 2007, 7:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.net.URL;
import java.nio.Buffer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.View;
import net.sf.xpontus.utils.MiscUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.vfs.VFS;
import org.apache.xml.resolver.Catalog;
import org.xml.sax.InputSource;


/**
 *
 * @author Owner
 */
public class CatalogManager
{
	//{{{ resolve() method
	public static InputSource resolve(String current,
		String publicId, String systemId)
		throws Exception
	{
		load();

		if(publicId != null && publicId.length() == 0)
			publicId = null;

		if(systemId != null && systemId.length() == 0)
			systemId = null;

		String newSystemId = null;

		/* we need this hack to support relative path names inside
		 * cached files. we want them to be resolved relative to
		 * the original system ID of the cached resource, not the
		 * cache file name on disk. */
		String parent;
		if(current != null)
		{
			Entry entry = (Entry)reverseResourceCache.get(current);
			if(entry != null)
				parent = entry.uri;
			else
				parent = MiscUtilities.getParentOfPath(current);
		}
		else
			parent = null;

		if(publicId == null && systemId != null && parent != null)
		{
			if(systemId.startsWith(parent))
			{
				// first, try resolving a relative name,
				// to handle jEdit built-in DTDs
				newSystemId = systemId.substring(
					parent.length());
				if(newSystemId.startsWith("/"))
					newSystemId = newSystemId.substring(1);
				newSystemId = resolveSystem(newSystemId);
			}
		}

		// next, try resolving full path name
		if(newSystemId == null)
		{
			if(publicId == null)
				newSystemId = resolveSystem(systemId);
			else
				newSystemId = resolvePublic(systemId,publicId);
		}

		// well, the catalog can't help us, so just assume the
		// system id points to a file
		if(newSystemId == null)
		{
			if(systemId == null)
				return null;
			else if(MiscUtilities.isURL(systemId))
				newSystemId = systemId;
			// XXX: is this correct?
			/* else if(systemId.startsWith("/"))
				newSystemId = "file://" + systemId;
			else if(parent != null && !MiscUtilities.isURL(parent))
				newSystemId = parent + systemId; */
		}

		if(newSystemId == null)
			return null;

		Buffer buf = jEdit.getBuffer(XmlPlugin.uriToFile(newSystemId));
		if(buf != null)
		{
//			if(buf.isPerformingIO())
//				VFSManager.waitForRequests();
////			Log.log(Log.DEBUG,CatalogManager.class,"Found open buffer for " + newSystemId);
			InputSource source = new InputSource(systemId);
			try
			{
				buf.readLock();
				source.setCharacterStream(new StringReader(buf.getText(0,
					buf.getLength())));
			}
			finally
			{
				buf.readUnlock();
			}
			return source;
		}
		else if(newSystemId.startsWith("file:")
			|| newSystemId.startsWith("jeditresource:"))
		{
			InputSource source = new InputSource(systemId);
			source.setByteStream(new URL(newSystemId).openStream());
			return source;
		}
		else if(!network)
			return null;
		else
		{
			final String _newSystemId = newSystemId;
			final VFS vfs = VFSManager.getVFSForPath(_newSystemId);
			// use a final array to pass a mutable value from the
			// invokeAndWait() call
			final Object[] session = new Object[1];
			Runnable run = new Runnable()
			{
				public void run()
				{
					View view = jEdit.getActiveView();
					if(!cache || showDownloadResourceDialog(
						view,_newSystemId))
					{
						session[0] = vfs.createVFSSession(
							_newSystemId,view);
					}
				}
			};

			if(SwingUtilities.isEventDispatchThread())
				run.run();
			else
			{
				try
				{
					SwingUtilities.invokeAndWait(run);
				}
				catch(Exception e)
				{
//					Log.log(Log.ERROR,CatalogManager.class,e);
				}
			}

			if(session[0] != null)
			{
				InputSource source = new InputSource(systemId);
				if(cache)
				{
					File file;
					try
					{
						file = copyToLocalFile(session[0],vfs,newSystemId);
					}
					finally
					{
						vfs._endVFSSession(session,null);
					}

					addUserResource(publicId,systemId,file.toURL().toString());
					source.setByteStream(new FileInputStream(file));
				}
				else
					source.setByteStream(vfs._createInputStream(session,newSystemId,false,null));

				return source;
			}
			else
				throw new IOException(jEdit.getProperty("xml.network-error"));
		}
	} //}}}

	 

	//{{{ isLocal() method
	public static boolean isLocal(Entry e)
	{
		if(e == null || jEdit.getSettingsDirectory() == null)
			return false;

		try
		{
			URL url = new File(jEdit.getSettingsDirectory()).toURL();
			String fileUrl = (String)resourceCache.get(e);
			return fileUrl.startsWith(url.toString());
		}
		catch (MalformedURLException ex)
		{
			return false;
		}
	} //}}}

	//{{{ propertiesChanged() method
	public static void propertiesChanged()
	{
		if(jEdit.getSettingsDirectory() == null)
		{
			cache = false;
		}
		else
		{
			resourceDir = MiscUtilities.constructPath(
				jEdit.getSettingsDirectory(),"dtds");
			cache = jEdit.getBooleanProperty("xml.cache");
		}
		network = jEdit.getBooleanProperty("xml.network");

		if(!cache)
			clearCache();

		loadedCatalogs = false;
	} //}}}

	//{{{ save() method
	public static void save()
	{
		if(loadedCache)
		{
			int systemCount = 0;
			int publicCount = 0;

			Iterator keys = resourceCache.keySet().iterator();
			while(keys.hasNext())
			{
				Entry entry = (Entry)keys.next();
				Object uri = resourceCache.get(entry);
				if(uri == IGNORE)
					continue;

				if(entry.type == Entry.PUBLIC)
				{
					jEdit.setProperty("xml.cache.public-id." + publicCount,entry.id);
					jEdit.setProperty("xml.cache.public-id." + publicCount
						+ ".uri",uri.toString());
					publicCount++;
				}
				else
				{
					jEdit.setProperty("xml.cache.system-id." + systemCount,entry.id);
					jEdit.setProperty("xml.cache.system-id." + systemCount
						+ ".uri",uri.toString());
					systemCount++;
				}
			}

			jEdit.unsetProperty("xml.cache.public-id." + publicCount);
			jEdit.unsetProperty("xml.cache.public-id." + publicCount + ".uri");
			jEdit.unsetProperty("xml.cache.system-id." + systemCount);
			jEdit.unsetProperty("xml.cache.system-id." + systemCount + ".uri");
		}
	} //}}}

	//{{{ clearCache() method
	public static void clearCache()
	{
		load();

		Iterator files = resourceCache.values().iterator();
		while(files.hasNext())
		{
			Object obj = files.next();
			if(obj instanceof String)
			{
				String file = (String)XmlPlugin.uriToFile((String)obj);
				Log.log(Log.NOTICE,CatalogManager.class,"Deleting " + file);
				new File(file).delete();
			}
		}
		resourceCache.clear();
	} //}}}

	//{{{ reloadCatalogs() method
	public static void reloadCatalogs()
	{
		loadedCatalogs = false;
	} //}}}
 

	//}}}

	//{{{ Private members

	//{{{ Static variables
	private static boolean loadedCache;
	private static boolean loadedCatalogs;
	private static boolean cache;
	private static boolean network;
	private static Catalog catalog;
	private static Set catalogFiles;
	private static HashMap resourceCache;
	private static HashMap reverseResourceCache;
	private static String resourceDir;

	// placeholder for DTDs we never want to download
	private static Object IGNORE = new Object(); 
	//}}}

	//{{{ addUserResource() method
	/**
	 * Don't want this public because then invoking {@link clearCache()}
	 * will remove this file, not what you would expect!
	 */
	private static void addUserResource(String publicId, String systemId, String url)
	{
		if(publicId != null)
		{
			Entry pe = new Entry( Entry.PUBLIC, publicId, url );
			resourceCache.put( pe, url );
		}

		Entry se = new Entry( Entry.SYSTEM, systemId, url );
		resourceCache.put( se, url );
		reverseResourceCache.put(url,se);
	} //}}}

	//{{{ copyToLocalFile() method
	private static File copyToLocalFile(Object session, VFS vfs, String path)
		throws IOException
	{
		if(jEdit.getSettingsDirectory() == null)
			return null;

		String userDir = jEdit.getSettingsDirectory();

		File _resourceDir = new File(resourceDir);
		if (!_resourceDir.exists())
			_resourceDir.mkdir();

		// Need to put this "copy from one stream to another"
		// into a common method some day, since other parts
		// of jEdit need it too...
		BufferedInputStream in = new BufferedInputStream(
			vfs._createInputStream(session,path,false,null));

		File localFile = File.createTempFile("cache", ".xml", _resourceDir);

		BufferedOutputStream out = new BufferedOutputStream(
			new FileOutputStream(localFile));

		byte[] buf = new byte[4096];
		int count = 0;
		while ((count = in.read(buf)) != -1)
			out.write(buf,0,count);
		out.close();

		return localFile;
	} //}}}

	//{{{ resolvePublic() method
	private static String resolvePublic(String systemId, String publicId)
		throws Exception
	{
		Entry e = new Entry(Entry.PUBLIC,publicId,null);
		String uri = (String)resourceCache.get(e);
		if(uri == null)
			return catalog.resolvePublic(publicId,null);
		else if(uri == IGNORE)
			return null;
		else
			return uri;
	} //}}}

	//{{{ resolveSystem() method
	private static String resolveSystem(String id) throws Exception
	{
		Entry e = new Entry(Entry.SYSTEM,id,null);
		String uri = (String)resourceCache.get(e);
		if(uri == null)
			return catalog.resolveSystem(id);
		else if(uri == IGNORE)
			return null;
		else
			return uri;
	} //}}}

	//{{{ showDownloadResourceDialog() method
	private static boolean showDownloadResourceDialog(Component comp, String systemId)
	{
		Entry e = new Entry(Entry.SYSTEM,systemId,null);
		return false;
	} //}}}

	//{{{ load() method
	private synchronized static void load()
	{
		if(!loadedCache)
		{
			loadedCache = true;

			resourceCache = new HashMap();
			reverseResourceCache = new HashMap();

			int i;
			String id, prop, uri;

			i = 0;
			while((id = jEdit.getProperty(prop = "xml.cache"
				+ ".public-id." + i++)) != null)
			{
				try
				{
					uri = jEdit.getProperty(prop + ".uri");
					resourceCache.put(new Entry(Entry.PUBLIC,id,uri),uri);
				}
				catch(Exception ex2)
				{
					Log.log(Log.ERROR,CatalogManager.class,ex2);
				}
			}

			i = 0;
			while((id = jEdit.getProperty(prop = "xml.cache"
				+ ".system-id." + i++)) != null)
			{
				try
				{
					uri = jEdit.getProperty(prop + ".uri");
					Entry se = new Entry(Entry.SYSTEM,id,uri);
					resourceCache.put(se,uri);
					reverseResourceCache.put(uri,se);
				}
				catch(Exception ex2)
				{
					//Log.log(Log.ERROR,CatalogManager.class,ex2);
				}
			}
		}

		if(!loadedCatalogs)
		{
			loadedCatalogs = true;

			catalog = new Catalog();
			catalogFiles = new HashSet();

			catalog.setupReaders();
			//catalog.setParserClass("org.apache.xerces.parsers.SAXParser");

			try
			{
				catalog.loadSystemCatalogs();

				catalog.parseCatalog("jeditresource:XML.jar!/xml/dtds/catalog");

				int i = 0;
				String prop, uri;
				while((uri = jEdit.getProperty(
					prop = "xml.catalog." + i++)) != null)
				{
////					Log.log(Log.MESSAGE,CatalogManager.class,
//						"Loading catalog: " + uri);

					try
					{
						if(MiscUtilities.isURL(uri))
							catalogFiles.add(uri);
						else
						{
							catalogFiles.add(
								MiscUtilities
								.resolveSymlinks(
								uri));
						}
						catalog.parseCatalog(uri);
					}
					catch(Exception ex2)
					{
////						Log.log(Log.ERROR,CatalogManager.class,ex2);
					}
				}
			}
			catch(Exception ex1)
			{
//				Log.log(Log.ERROR,CatalogManager.class,ex1);
			}
		}
	} //}}}

	//}}}
 

}

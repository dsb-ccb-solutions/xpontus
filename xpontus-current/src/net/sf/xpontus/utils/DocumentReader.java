package net.sf.xpontus.utils;

import java.io.Reader;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class DocumentReader extends Reader
  {
    /**
     * Current position in the document. Incremented whenever a character is
     * read.
     */
    private long position = 0;

    /**
     * Saved position used in the mark and reset methods.
     */
    private long mark = -1;

    /**
     * The document that we are working with.
     */
    private Document document;

    /**
     * Constructs a reader for the given document.
     *
     * @param document
     *            the document to be read.
     */
    public DocumentReader(Document document)
      {
        this.document = document;
      }

    /**
     * Returns the document underlying this DocumentReader.
     *
     * @return the underlying document.
     */
    public Document getDocument()
      {
        return document;
      }

    /**
     * Update the reader's state to be consistent with a change in the document.
     * The potential problem this addresses is if one thread modifies the
     * document while another thread is reading it.
     *
     * @param position
     *            the offset of the adjustment.
     * @param adjustment
     *            the distance of the adjustment. This will be negative for a
     *            deletion, positive for an insertion.
     */
    public void update(int position, int adjustment)
      {
        if (position < this.position)
          {
            if (this.position < (position - adjustment))
              {
                this.position = position;
              }
            else
              {
                this.position += adjustment;
              }
          }
      }

    /**
     * Does nothing. With a subsequent <code>seek</code>, we can still use
     * this Reader after the <code>close</code>. (It does reset the mark,
     * however, so that a subsequent <code>reset</code> will go to the
     * document's beginning.)
     */
    public void close()
      {
        mark = -1;
      }

    /**
     * Saves the current position for <code>reset</code>.
     *
     * @param readAheadLimit
     *            ignored.
     */
    public void mark(int readAheadLimit)
      {
        mark = position;
      }

    /**
     * Returns <code>true</code>, since this reader supports
     * <code>mark</code> and <code>reset</code>.
     *
     * @return <code>true</code>.
     */
    public boolean markSupported()
      {
        return true;
      }

    /**
     * Reads a single character and advances the internal cursor one step
     * forward.
     *
     * @return the next character code, or -1 if the end of the document has
     *         been reached.
     */
    public int read()
      {
        if ((document == null) || (position >= document.getLength()))
          {
            return -1;
          }
        else
          {
            try
              {
                char c = document.getText((int) position, 1).charAt(0);
                position++;

                return c;
              }
            catch (BadLocationException x)
              {
                return -1;
              }
          }
      }

    /**
     * Reads and fills the given buffer. This method always fills the buffer
     * unless the end of the document is reached.
     *
     * @param cbuf
     *            the buffer to fill.
     * @return the number of characters read or -1 if no more characters are
     *         available in the document.
     */
    public int read(char[] cbuf)
      {
        return read(cbuf, 0, cbuf.length);
      }

    /**
     * Reads and fills the buffer. This method always fills the buffer unless
     * the end of the document is reached.
     *
     * @param cbuf
     *            the buffer to fill.
     * @param off
     *            offset into the buffer to begin the fill.
     * @param len
     *            maximum number of characters to put in the buffer.
     * @return the number of characters read or -1 if no more characters are
     *         available in the document.
     */
    public int read(char[] cbuf, int off, int len)
      {
        if (document == null)
          {
            return -1;
          }
        else if (position < document.getLength())
          {
            int length = len;

            if ((position + length) >= document.getLength())
              {
                length = document.getLength() - (int) position;
              }

            if ((off + length) >= cbuf.length)
              {
                length = cbuf.length - off;
              }

            try
              {
                String s = document.getText((int) position, length);
                position += length;

                for (int i = 0; i < length; i++)
                  {
                    cbuf[off + i] = s.charAt(i);
                  }

                return length;
              }
            catch (BadLocationException x)
              {
                return -1;
              }
          }
        else
          {
            return -1;
          }
      }

    /**
     * Returns <code>true</code> since the Document is always available for
     * reading.
     *
     * @return <code>true</code>.
     */
    public boolean ready()
      {
        return true;
      }

    /**
     * Resets this reader to the last mark, or to the document's beginning if a
     * mark has not been set.
     */
    public void reset()
      {
        if (mark == -1)
          {
            position = 0;
          }
        else
          {
            position = mark;
          }

        mark = -1;
      }

    /**
     * Skips past characters in the document. This method always skips the
     * maximum number of characters unless the end of the file is reached.
     *
     * @param n
     *            maximum number of characters to skip.
     * @return the actual number of characters skipped.
     */
    public long skip(long n)
      {
        if (document == null)
          {
            return n;
          }
        else if ((position + n) <= document.getLength())
          {
            position += n;

            return n;
          }
        else
          {
            long oldPos = position;
            position = document.getLength();

            return (document.getLength() - oldPos);
          }
      }

    /**
     * Seeks to the given position in the document.
     *
     * @param n
     *            the offset to which to seek.
     */
    public void seek(long n)
      {
        if (document == null)
          {
            return;
          }
        else if (n <= document.getLength())
          {
            position = n;
          }
        else
          {
            position = document.getLength();
          }
      }
  }

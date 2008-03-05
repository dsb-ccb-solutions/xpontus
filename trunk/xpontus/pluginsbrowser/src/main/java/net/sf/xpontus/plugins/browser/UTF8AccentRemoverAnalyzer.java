/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.plugins.browser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
import java.io.Reader;

import java.util.Set;


public class UTF8AccentRemoverAnalyzer extends Analyzer {
    public final static String[] FRENCH_STOP_WORDS = {
            "a", "afin", "ai", "ainsi", "après", "attendu", "au", "aujourd",
            "auquel", "aussi", "autre", "autres", "aux", "auxquelles",
            "auxquels", "avait", "avant", "avec", "avoir", "c", "car", "ce",
            "ceci", "cela", "celle", "celles", "celui", "cependant", "certain",
            "certaine", "certaines", "certains", "ces", "cet", "cette", "ceux",
            "chez", "ci", "combien", "comme", "comment", "concernant", "contre",
            "d", "dans", "de", "debout", "dedans", "dehors", "delà", "depuis",
            "derrière", "des", "désormais", "desquelles", "desquels", "dessous",
            "dessus", "devant", "devers", "devra", "divers", "diverse",
            "diverses", "doit", "donc", "dont", "du", "duquel", "durant", "dès",
            "elle", "elles", "en", "entre", "environ", "est", "et", "etc",
            "etre", "eu", "eux", "excepté", "hormis", "hors", "hélas", "hui",
            "il", "ils", "j", "je", "jusqu", "jusque", "l", "la", "laquelle",
            "le", "lequel", "les", "lesquelles", "lesquels", "leur", "leurs",
            "lorsque", "lui", "là", "ma", "mais", "malgré", "me", "merci", "mes",
            "mien", "mienne", "miennes", "miens", "moi", "moins", "mon",
            "moyennant", "même", "mêmes", "n", "ne", "ni", "non", "nos", "notre",
            "nous", "néanmoins", "nôtre", "nôtres", "on", "ont", "ou", "outre",
            "où", "par", "parmi", "partant", "pas", "passé", "pendant", "plein",
            "plus", "plusieurs", "pour", "pourquoi", "proche", "près", "puisque",
            "qu", "quand", "que", "quel", "quelle", "quelles", "quels", "qui",
            "quoi", "quoique", "revoici", "revoilà", "s", "sa", "sans", "sauf",
            "se", "selon", "seront", "ses", "si", "sien", "sienne", "siennes",
            "siens", "sinon", "soi", "soit", "son", "sont", "sous", "suivant",
            "sur", "ta", "te", "tes", "tien", "tienne", "tiennes", "tiens",
            "toi", "ton", "tous", "tout", "toute", "toutes", "tu", "un", "une",
            "va", "vers", "voici", "voilà", "vos", "votre", "vous", "vu",
            "vôtre", "vôtres", "y", "à", "ça", "ès", "été", "être", "ô", "l'"
        };
    private Set stopSet;

    /** Creates a new instance of AccentUnicodeAnalyzer */
    public UTF8AccentRemoverAnalyzer() {
        this(FRENCH_STOP_WORDS);
    }

    /** Builds an analyzer with the given stop words. */
    public UTF8AccentRemoverAnalyzer(String[] stopWords) {
        stopSet = StopFilter.makeStopSet(stopWords);
    }

    /**
     *
     * Constructs a {@link StandardTokenizer} filtered by a {@link
     * StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}.
     *
     * @param fieldName
     * @param reader
     * @return
     */
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopSet);
        result = (TokenStream) new UTF8AccentRemoverFilter(result);

        return result;
    }
}

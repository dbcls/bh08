package roundtrip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.biojava.bio.seq.io.ParseException;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.FiniteAlphabet;
import org.biojavax.Namespace;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.io.EMBLFormat;
import org.biojavax.bio.seq.io.EMBLxmlFormat;
import org.biojavax.bio.seq.io.FastaFormat;
import org.biojavax.bio.seq.io.GenbankFormat;
import org.biojavax.bio.seq.io.INSDseqFormat;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.bio.seq.io.RichSequenceFormat;
import org.biojavax.bio.seq.io.RichStreamReader;
import org.biojavax.bio.seq.io.RichStreamWriter;
import org.biojavax.bio.seq.io.UniProtFormat;
import org.biojavax.bio.seq.io.UniProtXMLFormat;
import org.biojavax.bio.taxa.NCBITaxon;
import org.biojavax.bio.taxa.io.NCBITaxonomyLoader;
import org.biojavax.bio.taxa.io.SimpleNCBITaxonomyLoader;

/**
 * This program will round trip sequence formats
 * @author Mark
 */
public class Main {

    /**
     * Attempts to find a format for a name String such as "genbank" or for a
     * fully qualified string like org.biojavax.bio.seq.io.UniProtFormat
     * @return the matching <code>RichSequenceFormat</code>
     * @param name the name of the format, case insensitive except for qualified class names
     * @throws java.lang.IllegalAccessException If java cannot reflectively access the named format.
     * Only applies to fully qualified class names.
     * @throws java.lang.ClassNotFoundException If a format can not be found for the name.
     * @throws java.lang.InstantiationException If the found object cannot be created (only applies
     * to fully qualified class names).
     */
    public static RichSequenceFormat formatForName(String name)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //determine the format to use
        RichSequenceFormat format;
        if (name.equalsIgnoreCase("fasta")) {
            format = new FastaFormat();
        } else if (name.equalsIgnoreCase("genbank")) {
            format = new GenbankFormat();
        } else if (name.equalsIgnoreCase("uniprot")) {
            format = new UniProtFormat();
        } else if (name.equalsIgnoreCase("embl")) {
            format = new EMBLFormat();
        } else if (name.equalsIgnoreCase("INSDseq")) {
            format = new INSDseqFormat();
        } else if (name.equalsIgnoreCase("EMBLxml")) {
            format = new EMBLxmlFormat();
        } else if (name.equalsIgnoreCase("UniprotXML")){
            format = new UniProtXMLFormat();
        } else {
            Class formatClass = Class.forName(name);
            format = (RichSequenceFormat) formatClass.newInstance();
        }
        return format;
    }

    public static void loadNCBITaxon() throws IOException, ParseException{
        NCBITaxonomyLoader l = new SimpleNCBITaxonomyLoader();
        BufferedReader nodes = new BufferedReader(new FileReader("nodes.dmp"));
        BufferedReader names = new BufferedReader(new FileReader("names.dmp"));

        NCBITaxon t;
        while ((t = l.readNode(nodes)) != null) {}  // read all the nodes first
        while ((t = l.readName(names)) != null) {}  // then read all the names 
    }

    /**
     * @param args the command line arguments
     * args[0] the input file name
     * args[1] the input format name or fully qualified classname (eg fasta, or 
     * org.biojavax.bio.seq.io.FastaFormat)
     * args[2] the ouput format name (see above)
     * args[3] the alphabet (commonly DNA or Protein)
     * args[4] the namespace (something like gb)
     */
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        RichSequenceFormat inFormat = formatForName(args[1]);
        RichSequenceFormat outFormat = formatForName(args[2]);
        FiniteAlphabet alpha = (FiniteAlphabet) AlphabetManager.alphabetForName(args[3]);
        Namespace ns = null;
        SymbolTokenization toke = alpha.getTokenization("default");
        
        if(! (inFormat.getClass().equals(formatForName("fasta").getClass()) 
                || outFormat.getClass().equals(formatForName("fasta").getClass()))){
            System.out.println("Loading NCBI taxonomy");
            loadNCBITaxon();
        }

        RichStreamReader sr = new RichStreamReader(
                br, inFormat, toke,
                RichSequenceBuilderFactory.THRESHOLD,
                null);
        
        RichStreamWriter sw = new RichStreamWriter(System.out, outFormat);
        sw.writeStream(sr, ns);
    }
}

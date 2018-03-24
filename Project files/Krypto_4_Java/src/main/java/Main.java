import models.Project;
import models.analytics.CalculateIC;
import models.analytics.FrequencyAnalysis;
import models.ciphers.*;

/**
 * Copyright (C) 2018 Krypto.
 */


/**
 * Main class.
 *
 * @version 1.0.0
 */
public final class Main {

  /**
   * s
   * Private constructor to prevent instantiation.
   */
  private Main() {
    //do nothing
  }

  /**
   * Main code.
   *
   * @param args command-line arguments
   */
  public static void main(final String[] args) {

    //Initialization
    AbstractCipher cipher = new TranspositionPeriodic();
    String projectName = "TEST_PROJECT";
    //CHECKSTYLE:OFF
    int identifier = 1234;
    //CHECKSTYLE:ON
    String key = "sEcret";
    String ciphertext = "abbccc a b c ab ac ba bc ca cb abc bac";
//    String ciphertext = "KQMXKEYMLQQYXSPGLMNMBNSWOMQMIJOFPKXCNMRCCKZERRMXOGMXWGNIPOHKKCCMWRKXGMHGNWSBTPSWCOPCQELMIRRIGQRMBELDEEOSUXLCBQGCWAYPBVEQDMRCSLEQCBSSCMDRIMEXJSZCNHGCTMCEJRSULYRCSLCQPCPYNCURILRIPOWNOGGKPJIEPOYLZPCKWYXXMEXYVXCBERSSLMSLDMLEMLQYLBIQOVTOHPOWMVYRSSLRILMIFYTCCRMSWWWEWMLGXEDEPJIELNEKSXPOKYBHQDEGBWZBELMLRRMPDCJOREDLYPJMBHKKVGKRLOSPRYQLELNWGPERCXPYREOVWOGMXWGNIPOHGCEQWMBNPCDSLCYLMSKWSLVCNBSKYXGYRNOVDOGRVCWOGMXWGCXCNWMRMQMLYDXWNMLSREPSPOJDOGRVEBSIQKGRSZCOUSKPJINMEVLOCUSWFSREXSRCITOVYVFCRETOHARENDIPCLCDAMCMPNIDSGGORRZVMMYPSREPETYYPSXCOBRORQSZCISSDAMIIRNMKSRSDMMXWFOMKZSQCMZVISXHCBWRYSBKKCKVPSZCNGMWTYCWNBINKVCKRMXEQBIYCSLKFJOTYBXGMYJKVMXQWSXGXWWWTYDLGJIQSDCXSUOEQIIYDLYXHFYASXAGVPGXKFOHCZEPDYPOIJCIURIPOHCTIADMMXERRIYBXJKVEOWCOQQWEWZYPCIKOELCJCGFJSRBOBOEMQSXCXIUCTYZIPKXRORBSREYRAOVRKMLDCMRWSCTGMMMXSDRIJOWQNSOEMROZGVMQKHBWERDIPPEKSPWKGRSZCWYREEJZYRGMQRIQRENZILKGAOTRKRAOQGNHJOXMXWKOMDNMQMVCDMMXFMSWROVMEWRBETOPJSREKRQRINBSQZIPYYQMSLDMLEMLQILDVCKXGOWAYQNKRGYRQERPOWCBZCNCMEFMSWROVMEWKSHBVIRYRQZSPDWKORQSVLYAAYVBSEJVCYCOYNHGDMMXWDYVWYYRORMMGYCMMXEJCEUOZCBCRRMLQFSDGMXZGMXGYRBKYERXCBVCDYPXIBAYGDXGXKDOAYBIBKCYNZYXGCNFPKRARIBNSCXNMIQCXXBOJCMXGFIMLNCMXGYRMBACSJDKZMEVGDIYDAMXHCBEDPSPNWMNELQIPMELXSRPSPWIPCICSREZSUOVTSWGDGFKVKWSLOCYNHFOEPNRCGSRRIPZYRKXRORBOHLYMLNYJQIBWEPBMYQIGCXMTYBQQCXXMPJCBMLQPYXHJYVBISSNMQZSQKPQDVMXKJIUSSXRSRERMQORBOETYVRGSQOXRVMLQLGWQYXRCBWFKQFSQFOEPDIBRYLNVCNIVZILCIEOXMZILQEKOLGWAFKXFYYPWSPOTYBXYNENDIBKWQWMJSREYJDOQYVIQYLKONMEVLOCCHTMCIBMSLMIPXQCDGMWIYNHAYPBMEJWVMCIKSPCGLYDXGVIBWELYVAYYPDERLYGVXZITJKGCPELXCBSWABIRSSLKXZOELCSBOGGCMTOPWOWNOGGKPJIIVOXCBMRCIJPSZTIADQYDXCBMDYRKBMLKXRKGFWILDENKVRWILDWGXHCVMERXDEPZIQMDMMXPCCWGDRMKRBXSUCLCLYPCXQSVJOEPXXMDEJRIYBMLQLCKVROHQRIUSREYALKWICSJSGGDYBOYLMSKWSLVCSCIFOVKYXGYRJOWQXSRMSJVIADMLQEEOXFOTPYTCBPWCIPFELDWPOUSSVCNQGCXYUILYYRVMTOHZOHYXHPOQYSRBOVYNQGDXGXKLOKJOGROHGCLCLIJYRESREDSNOVNOXSKPMLNCMXGYRSZLYCAGNILDSMISSNIAKCZOKGXAFSGFKWIOHCAYYVELIWRBSLQIPERNKGIOHDOPGMMRIXMYJKSWRKOCXJYXRWKXUBSLQXYLPCIIGXFCYRCKWGVCAKRLYXGXRYDIGXPYCXCNQMXXFCSLNMDPIPOHYXHYXHDOPGMMRIWROINOWRWVQKKCYYRGIGQLMZMLSSLCPCKVLSREVMIOAGCIBKYERXCBRMGEEOSSDACSKFBENDYPOWQDELRMJVQWQVCKXCCXKSWRKOCXSPOBCBGGCIFOSLKPRRSSQLBSWAYYPCIMDLCBAGCIBSWNYWGXKYCMRYJQDVYXKCBWDYVDOMROHBOJGMMCXXSZEKSRRORRSSLYRBOTCXHCXXOEIQDMMXWMRIJCIURIPOWCZXCWFCBRMLIRBEWOHNVIYCYPOTMCWGLPCTSGXXSBIUOMLDLPYAGXKYXHAKRCFILDVYZMBKRWCLYVPUYQYXKPOILRSNOXFOCBOEPGLMSXQLVCNWKSPGXKLYXFSREKJDSBCNLCMEPBMCNMRMPMDLCCGYVPGXKFORMSXQCSKOXFSRENMQZSQSRENINKVREVCCLCPETYYPSXCDSJOVYLPWOREBSQCIBDVSDLQRSPDJMVPWMSSBXURCQRIRRIGBFYVPQOBAOPJORAOTSDYLKJDOGROHPOEQYRYLPCWVQSRRBSBEGCNGMXZGMXGYRQRILKCNKVRSGSVEPNIJSKFDJSVFSDYLZPCKWYXXDYVSXGMWQMXPWGLMKQGPRSWFCBRMETNOVGYHPOKYBHQEHBORZOXROVBOGGCMTOPWCYPBSSXHCNEJVEBWMPKXGYRYXHLYXWYYMEXNKVRSGSVEPCCKZERRMXORMDJYFSSBEZVIGXXPYHSMIBSRQSTGNMRIFSDLYWVYDLCBRSWFCBGYXELNWCDTPKMQOHGCXPEWRCELSXAYRRORROHNOVAOMTOHYDXCXHGXKMRXFYVMEKFVCCCXGWERSRESRRBSBEGCNWRSQSVEROHURCZEXKYXGYRJOWQMESCIBBMCNRMCSJSHLYELCQYVPQYWRSPJGMBORRORUOERRIPOZGNILDWKSPGXKZOHYQEGXWRCLCOBYWMLOMRCVCXHCBIBPEPYTGXMMXWRGSWOXKYHCBEROWCHWRBMISRECYDPMASILDQMDMMXPCCWAYQNVMKORRLCQDMKEPYDIBKWQSWRKRAOERMSLFMLMIBBIQYPTSREOBRORQSZCKKPOIYLPCSRGDSLKWPOQYSRBOVAYVBSEJVCQKCYPJCMXGYRKOXURSNBSNBMCDCFSQYBIKKRQRIRYAYBHQZVGFEROACKXFOVNVIYCIBSRKYVCZEPDLCVSQORCOHQYAYXXPKRIXSYDFPSRESREYVFOWCXWGLPCZPCKWSBINBITORRRINKVJYVQNSUKMRSRELIDOQYVIQKRKOWQKKCCSASIRI";
    Project project = new Project(projectName, identifier, ciphertext);

    System.out.println("project = " + project.getModifiedText());

    System.out.println(CalculateIC.getIC(project.getModifiedText(), 3));
    System.out.println(FrequencyAnalysis.frequencyAnalysis(project.getModifiedText(), 1));
    System.out.println(FrequencyAnalysis.frequencyAnalysis(project.getModifiedText(), 2));
    System.out.println(FrequencyAnalysis.frequencyAnalysis(project.getModifiedText(), 3));
  }

}

package cn.touch.demo.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2021/10/25.
 */
public class Lucene {
    private static SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

    private static Path path = Paths.get("idx");

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {


        analyzer();
//        createIndex();

//        search("金庸");

    }

    private static void analyzer() throws IOException {
        //        Version matchVersion = Version.LUCENE_7_7_2; // Substitute desired Lucene version for XY
        Analyzer analyzer = new StandardAnalyzer(); // or any other analyzer
        TokenStream ts = analyzer.tokenStream("myfield", new StringReader("some text goes here"));
        // The Analyzer class will construct the Tokenizer, TokenFilter(s), and CharFilter(s),
        //   and pass the resulting Reader to the Tokenizer.
        OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);

        try {
            ts.reset(); // Resets this stream to the beginning. (Required)
            while (ts.incrementToken()) {
                // Use AttributeSource.reflectAsString(boolean)
                // for token stream debugging.
                System.out.println("token: " + ts.reflectAsString(true));

                System.out.println("token start offset: " + offsetAtt.startOffset());
                System.out.println("  token end offset: " + offsetAtt.endOffset());
            }
            ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
        } finally {
            ts.close(); // Release resources associated with this stream.
        }
    }

    private static void createIndex() throws IOException {
//        FSDirectory dir = FSDirectory.open(Paths.get("idx"))
//        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(analyzer));

        try (FSDirectory indexDirectory = FSDirectory.open(path); IndexWriter indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
            File file = new File("src/main/resources/jinyong");
            System.err.println(file.getAbsoluteFile());
            BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(file)));
            String line = reader.readLine();
            int i = 1;
            List<Document> list = new ArrayList<>();
            while (line != null && line.trim() != "") {
                Document doc = new Document();
                doc.add(new StringField("line", Integer.toString(i), Field.Store.YES));
                doc.add(new TextField("content", line, Field.Store.YES));
                list.add(doc);
                line = reader.readLine();
                i++;
            }
            indexWriter.addDocuments(list);

//            indexWriter.updateDocument()
//            indexWriter.updateDocuments()

        }


    }

    private static void search(String q) throws ParseException, IOException, InvalidTokenOffsetsException {
        try (FSDirectory indexDirectory = FSDirectory.open(path); IndexReader indexReader = DirectoryReader.open(indexDirectory);) {
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new StandardAnalyzer();

//            多字段查询
//            MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser({"line", "content"}, analyzer);
//            Query query = multiFieldQueryParser.parse(q);

            QueryParser queryParser = new QueryParser("content", analyzer);
            Query query = queryParser.parse(q);

            long start = System.currentTimeMillis();
//            Sort sort = new Sort(new SortField("line", SortField.Type.LONG, false));
//            TopDocs topDocs = indexSearcher.search(query, 100, sort);
            TopDocs topDocs = indexSearcher.search(query, 100);
            long end = System.currentTimeMillis();
            System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + topDocs.totalHits + "个记录");



            QueryScorer queryScorer = new QueryScorer(query);
            // 得分项对应的内容片段
            SimpleSpanFragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
            // 高亮显示的样式
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"background-color:yellow\"><b>", "</b></span>");
            // 高亮显示对象
            Highlighter highlighter = new Highlighter(htmlFormatter, queryScorer);
            highlighter.setTextFragmenter(fragmenter);

//            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docID = scoreDoc.doc;
                Document doc = indexSearcher.doc(docID);
//                System.out.println("第：" + doc.get("line") + "行，查到数据:"+doc.get("content"));
                System.out.println("第：" + doc.get("line") + "行，查到数据(高亮显示):"+highlighter.getBestFragment(analyzer, "content", doc.get("content")));
            }
        }
    }

    private static void delIndex() throws IOException {
        try (FSDirectory indexDirectory = FSDirectory.open(path); IndexWriter indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
            indexWriter.deleteAll();
        }
    }
}

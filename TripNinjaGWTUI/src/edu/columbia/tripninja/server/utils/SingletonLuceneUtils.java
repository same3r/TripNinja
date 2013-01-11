package edu.columbia.tripninja.server.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.columbia.tripninja.shared.Constants;

public class SingletonLuceneUtils {

	private IndexWriter indexWriter = null;
	private IndexReader indexReader = null;
	private Directory indexDir = null;

	private SingletonLuceneUtils() {

	}

	public static SingletonLuceneUtils getSingletonObject() {
		if (ref == null)

			ref = new SingletonLuceneUtils();
		return ref;
	}

	private static SingletonLuceneUtils ref;

	public IndexWriter getIndexWriter() throws IOException {

		if (indexWriter == null) {

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

			indexWriter = new IndexWriter(
					getIndexDir(Constants.LUCENE_INDEX_DIR), config);
		}
		return indexWriter;
	}

	public IndexReader getIndexReader() throws IOException {

		if (indexReader == null) {
			indexReader = IndexReader
					.open(getIndexDir(Constants.LUCENE_INDEX_DIR));
		}
		return indexReader;
	}

	public static ArrayList<String> searchPlaces(String queryStr)
			throws ParseException, IOException {

		ArrayList<String> placeList = new ArrayList<String>();
		Query q = new QueryParser(Version.LUCENE_36,
				Constants.LUCENE_ARTICLE_FEILD, new StandardAnalyzer(
						Version.LUCENE_36)).parse(queryStr);

		int hitsPerPage = 10;
		SingletonLuceneUtils luceneUtils = SingletonLuceneUtils
				.getSingletonObject();
		IndexReader reader = luceneUtils.getIndexReader();
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String placeId = d.get(Constants.LUCENE_DOCID_FEILD);
			System.out.println((i + 1) + ". " + placeId);
			placeList.add(placeId);
		}

		searcher.close();
		return placeList;
	}

	public static ArrayList<String> searchNinjasPick(int rand)
			throws ParseException, IOException {

		ArrayList<String> placeList = new ArrayList<String>();
		SingletonLuceneUtils luceneUtils = SingletonLuceneUtils
				.getSingletonObject();
		IndexReader reader = luceneUtils.getIndexReader();
		IndexSearcher searcher = new IndexSearcher(reader);
		Document doc = searcher.doc(rand);
		placeList.add(doc.get(Constants.LUCENE_DOCID_FEILD));
		searcher.close();
		return placeList;
	}

	public static String fetchDesc(String placeId) throws ParseException,
			IOException {

		Term term = new Term(Constants.LUCENE_DOCID_FEILD, placeId);
		TermQuery query = new TermQuery(term);
		String ret = "";

		int hitsPerPage = 1;
		SingletonLuceneUtils luceneUtils = SingletonLuceneUtils
				.getSingletonObject();
		IndexReader reader = luceneUtils.getIndexReader();
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		System.out.println("Found " + hits.length + " hits.");
		if (hits.length > 0) {
			int docId = hits[0].doc;
			Document d = searcher.doc(docId);

			ret = d.get(Constants.LUCENE_DESC_FEILD);
		}

		searcher.close();
		return ret;
	}

	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			try {
				indexWriter.close();
			} catch (AlreadyClosedException ace) {
				System.out.println("Index Writer Closed");
			}
		}
	}

	public void closeIndexReader() throws IOException {
		if (indexReader != null) {
			indexReader.close();
			System.out.println("Index Reader Closed");
		}
	}

	public Directory getIndexDir(String lUCENE_INDEX_DIR) throws IOException {
		if (indexDir == null) {
			indexDir = FSDirectory.open(new File(Constants.LUCENE_INDEX_DIR));
		}
		return indexDir;
	}

}

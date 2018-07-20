package com.fuzzySearch.core;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class App {
	
   String indexDir = "/home/emre/DEV/Data"; //give your path
   String dataDir = "/home/emre/DEV/Data";  // give your path
   Indexer indexer;
   Searcher searcher;

   public static void main(String[] args) {
      App tester;
      try {
         tester = new App();
         tester.createIndex();
         tester.search("McKinle");
         tester.searchUsingFuzzyQuery("McKinle");
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   private void createIndex() throws IOException {
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }

   private void search(String searchQuery) throws IOException, ParseException {
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: "
            + doc.get(LuceneConstants.FILE_PATH));
      }
      searcher.close();
   }
   
   private void searchUsingFuzzyQuery(String searchQuery)
		      throws IOException, ParseException {
		      searcher = new Searcher(indexDir);
		      long startTime = System.currentTimeMillis();
		      Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
		      Query query = new FuzzyQuery(term);
		      TopDocs hits = searcher.search(query);
		      long endTime = System.currentTimeMillis();

		      System.out.println(hits.totalHits +
		         " documents found. Time :" + (endTime - startTime) + "ms");
		      for(ScoreDoc scoreDoc : hits.scoreDocs) {
		         Document doc = searcher.getDocument(scoreDoc);
		         System.out.print("Score: "+ scoreDoc.score + " ");
		         System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
		      }
		      searcher.close();
		   }
}
package webCrawler;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;



public class PaperCrawler1 {
    public ArrayList<ArrayList<String>> crawler(BufferedWriter bufferedWriter, Integer TotalNum, String Query) throws IOException {		
        String url = "https://dblp.org/search//inc"; 		// 网页数据包url
        Integer page = 1;
        
        // 储存所有数据
        ArrayList<ArrayList<String>> Data = new ArrayList<ArrayList<String>>();
        try {
        	Integer Num = 0;        	
        	while(Num < TotalNum) {
        		Map<String, String> params = new HashMap<>();
            	if(Num == 0) {								// 构建GET请求参数
                	params.put("q", Query);
                	params.put("h", "30");
                	params.put("f", "0");
                	params.put("s", "ydvspc");
            	}else {
                	params.put("q", Query);
                	params.put("s", "ydvspc");
                	params.put("h", "30");
                	params.put("b", page.toString());
                	page ++;
            	}
	        	Document doc = null;
	        	doc =  getJsoupDocGet(url, params);			// 请求数据
	        	//System.out.println(doc);	            
	
	            // 获取页面中所有 class 为 "entry article toc" 的元素
	            Elements articleElements = doc.select("li.entry.article.toc, li.entry.informal.toc, li.entry.inproceedings.toc");
	
	            // 遍历每个文章元素，提取所需信息
	            for (Element article : articleElements) {
	            	// 储存单条数据信息
	            	ArrayList<String> SingleData = new ArrayList<>();
	            	
	                // 获取DOI号
	                String doi = article.select("a[itemprop=url]").attr("href");
	                SingleData.add(doi);
	
	                // 获取论文题目
	                String title = article.select("span.title[itemprop=name]").text();
	                SingleData.add(title);
	
	                // 获取作者信息
	                Elements authors = article.select("span[itemprop=author] span[itemprop=name]");
	                StringBuilder authorList = new StringBuilder();
	                for (Element author : authors) {
	                    authorList.append(author.text()).append(", ");
	                }
	                // 删除最后的逗号和空格
	                if (authorList.length() > 2) {
	                    authorList.setLength(authorList.length() - 2);
	                }
	                SingleData.add(authorList.toString());
	
	                // 获取年份
	                String year = article.select("span[itemprop=datePublished]").text();
	                SingleData.add(year);
	
	                // 获取期刊名称
	                String journalName = article.select("span[itemprop=isPartOf] span[itemprop=name]").text();
	                SingleData.add(journalName);
	
	                // 打印提取的信息
	                if (article.hasClass("article")) {
//	                    System.out.println("Type: Journal Paper");
	                	bufferedWriter.write("Type: Journal Paper\n");
	                	SingleData.add("Journal Paper");
//	                    System.out.println("DOI: " + doi);
	                	bufferedWriter.write("DOI: " + doi + "\n");
//	                    System.out.println("Title: " + title);
	                	bufferedWriter.write("Title: " + title + "\n");      
//	                    System.out.println("Authors: " + authorList);
	                	bufferedWriter.write("Authors: " + authorList + "\n");    
//	                    System.out.println("Year: " + year);
	                	bufferedWriter.write("Year: " + year + "\n");    
//	                    System.out.println("Journal: " + journalName);
	                	bufferedWriter.write("Journal: " + journalName + "\n");    
//	                    System.out.println("--------------------");
	                	bufferedWriter.write("--------------------\n");    
	                } else if (article.hasClass("inproceedings")) {
//	                    System.out.println("Type: Conference Paper");
	                	bufferedWriter.write("Type: Conference Paper");
	                	SingleData.add("Conference Paper");
//	                    System.out.println("DOI: " + doi);
	                	bufferedWriter.write("DOI: " + doi + "\n");
//	                    System.out.println("Title: " + title);
	                	bufferedWriter.write("Title: " + title + "\n");      
//	                    System.out.println("Authors: " + authorList);
	                	bufferedWriter.write("Authors: " + authorList + "\n");    
//	                    System.out.println("Year: " + year);
	                	bufferedWriter.write("Year: " + year + "\n");    
//	                    System.out.println("Conference: " + journalName);
	                	bufferedWriter.write("Conference: " + journalName + "\n");    
//	                    System.out.println("--------------------");
	                	bufferedWriter.write("--------------------\n");    
	                } else if (article.hasClass("informal")) {
//	                    System.out.println("Type: ArXiv Paper");
	                	bufferedWriter.write("Type: ArXiv Paper");
	                	SingleData.add("ArXiv Paper");
//	                    System.out.println("DOI: " + doi);
	                	bufferedWriter.write("DOI: " + doi + "\n");
//	                    System.out.println("Title: " + title);
	                	bufferedWriter.write("Title: " + title + "\n");      
//	                    System.out.println("Authors: " + authorList);
	                	bufferedWriter.write("Authors: " + authorList + "\n");    
//	                    System.out.println("Year: " + year);
	                	bufferedWriter.write("Year: " + year + "\n");    
//	                    System.out.println("Pre-print: " + journalName);
	                	bufferedWriter.write("Pre-print: " + journalName + "\n");    
//	                    System.out.println("--------------------");
	                	bufferedWriter.write("--------------------\n");    
	                }
	                Data.add(SingleData);
	            }
	            Num += articleElements.size();
	            
        	}
        	System.out.println("Total Page: " + page);			// 打印请求总页数
            System.out.println("Total number of data: " + Num);	// 打印数据总条数
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return Data;
    }

    public static Document getJsoupDocGet(String url, Map<String, String> params) {
        //三次试错
        final int MAX = 10;
        int time = 0;
        Document doc = null;
        while (time < MAX) {
            try {
                doc = Jsoup
                        .connect(url)
                        .data(params)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(1000 * 30)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("accept-encoding","gzip, deflate, br")
                        .header("accept-language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                        .get();
                return doc;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                time++;
            }
        }
        return doc;
    }
}

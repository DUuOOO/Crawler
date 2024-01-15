package webCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import webCrawler.DatabaseHandler.Paper;
// 爬取Knowledge-Based Systems相关论文8000+条
// 结果在results.txt 文件中

public class MainCrawler {
	public static void main(String[] args) throws IOException {
		Integer TotalNum = 10;
		String Query = "Philip S. Yu";	// 查询信息
		// 写入数据库数据
		ArrayList<ArrayList<String>> Data = new ArrayList<ArrayList<String>>();
		// 数据库连接信息
	    String JDBC_URL =  "jdbc:mysql://localhost:3306/mysql";
	    String USERNAME =  "root";
	    String PASSWORD = "root";
		DatabaseHandler db = new DatabaseHandler(JDBC_URL, USERNAME, PASSWORD);
		
    	System.out.println("Start to Crawler!");
    	String filepath = "./output/test.txt";
    	File outputfile = new File(filepath);
		if (!outputfile.exists()) {
			outputfile.createNewFile();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputfile));
		PaperCrawler1 PaperCrawler = new PaperCrawler1();
		try {
			// 爬取数据
			Data = PaperCrawler.crawler(bufferedWriter, TotalNum, Query);
		// 插入、增加操作。输出爬取的数据列表，将数据传入数据库
		for(ArrayList<String> SingleData: Data) {
//			System.out.println(SingleData.toString());
			db.insertPaper(SingleData.get(5),SingleData.get(0), SingleData.get(1), SingleData.get(2), SingleData.get(3),
					SingleData.get(4));
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		删除操作测试
//		db.deletePaper("999ebe50-72ea-4");
//		System.out.println("Delete successfully!");
		
//		查询操作测试		
//		List<Paper> papers = db.getAllPapers();
//		for(Paper paper: papers) {
//			System.out.println(paper.getId() + ",\t" + paper.getType() + ",\t" + paper.getDoi() + ",\t" + paper.getTitle() +
//					",\t" + paper.getAuthors() + ",\t" + paper.getYear() + ",\t" + paper.getJournal());
//		}
		
//		修改操作测试
//		[c6906b2d-7897-4,Journal Paper,https://doi.org/10.1145/3603620,Machine Unlearning: A Survey.,
//			(Heng Xu, Tianqing Zhu, Lefeng Zhang, Wanlei Zhou, Philip S. Yu),2024,ACM COMPUTING SURVEYS]
//		Paper UpdateData = new Paper();
//		UpdateData.setId("c6906b2d-7897-4");
//		UpdateData.setType("Journal Paper");
//		UpdateData.setDoi("https://doi.org/10.1145/3603620");
//		UpdateData.setTitle("Machine Unlearning: A Survey.");
//		UpdateData.setAuthors("Heng Xu, Tianqing Zhu, Lefeng Zhang, Wanlei Zhou, Philip S. Yu");
//		UpdateData.setYear("2024");
//		UpdateData.setJournal("ACM COMPUTING SURVEYS");
//		db.updatePaper(UpdateData);
//		System.out.println("Update successfully!");
				
	}
	
}

package webCrawler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHandler {
    // 数据库连接信息
    private String JDBC_URL;	// "jdbc:mysql://your_database_url"
    private String USERNAME;	// "your_username"
    private String PASSWORD;	// "your_password"
	
	public DatabaseHandler(String jdbc_url, String username, String password) {
		this.JDBC_URL = jdbc_url;
		this.USERNAME = username;
		this.PASSWORD = password;		
		
		if(validateTableExist("Paper") == true) { // 假如数据表不存在，建表
	        try (Connection connection = DriverManager.getConnection(jdbc_url,username, password)){
	    		// 建表语句
	        	String tableSql = "create table `Paper` (id varchar(16), type varchar(20), doi varchar(400), title varchar(400),"
	        			+ " authors varchar(400), year varchar(5), journal varchar(100));";
	            try (PreparedStatement statement = connection.prepareStatement(tableSql)) {
	                // 执行建表语句
	                statement.executeUpdate();
	            }
	        } catch (SQLException e) {
				e.printStackTrace();
			}
	        System.out.println("Create table `Paper` successfully!");
		}
		
	}
	
    /**
     * 用uuid生成为16位id
     * eg 49abc3f3-d6cc-4401-b24a-0fc808d1b594
     */
    public String UUID(){
        String uuid = UUID.randomUUID().toString().substring(0,15);
//        System.out.println("uuid:"+uuid);
        return uuid;

    }

    public boolean validateTableExist(String tableName){
        boolean flag = false;
        try(Connection conn = DriverManager.getConnection(JDBC_URL,USERNAME, PASSWORD)) {
	            DatabaseMetaData meta = conn.getMetaData();
	            String type [] = {"TABLE"};
	            flag = meta.getTables(null, null, tableName, type).next();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        return flag;
    }


	// 插入论文信息到数据库
    public void insertPaper(String type, String doi, String title, String authors, String year, String journal) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // 使用预处理语句插入数据，防止 SQL 注入攻击
        	String sql = "INSERT INTO Paper (id, type, doi, title, authors, year, journal) "
        			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
            	String UUId = UUID();
                statement.setString(1,UUId);
                statement.setString(2, type);
                statement.setString(3, doi);
                statement.setString(4, title);
                statement.setString(5, authors);
                statement.setString(6, year);
                statement.setString(7, journal);

                // 执行插入操作
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 删除论文信息
    public void deletePaper(String paperId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM Paper WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, paperId);

                // 执行删除操作
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取所有论文信息
    public List<Paper> getAllPapers() {
        List<Paper> papers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // 使用预处理语句查询数据，防止 SQL 注入攻击
            String sql = "SELECT * FROM Paper";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Paper paper = new Paper();
                        // 设置论文属性
                        paper.setId(resultSet.getString("id"));
                        paper.setType(resultSet.getString("type"));
                        paper.setDoi(resultSet.getString("doi"));
                        paper.setTitle(resultSet.getString("title"));
                        paper.setAuthors(resultSet.getString("authors"));
                        paper.setYear(resultSet.getString("year"));
                        paper.setJournal(resultSet.getString("journal"));

                        // 将论文对象添加到列表
                        papers.add(paper);
                    }
                }
            }
        } catch (SQLException e) {
        	e.getMessage();
        }
        return papers;
    }

    // 更新论文信息
    public void updatePaper(Paper updatedPaper) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE Paper SET type = ?, doi = ?, title = ?, authors = ?, year = ?, journal = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, updatedPaper.getType());
                statement.setString(2, updatedPaper.getDoi());
                statement.setString(3, updatedPaper.getTitle());
                statement.setString(4, updatedPaper.getAuthors());
                statement.setString(5, updatedPaper.getYear());
                statement.setString(6, updatedPaper.getJournal());
                statement.setString(7, updatedPaper.getId());

                // 执行更新操作
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 关闭数据库连接
    public void closeConnection() {
        try {
            DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    static class Paper {
		private String id;		// 数据唯一标识符号
        private String type;    // 论文类型（期刊论文、会议论文等）
        private String doi;     // 论文的DOI号
        private String title;   // 论文标题
        private String authors; // 论文作者
        private String year;    // 论文发表年份
        private String journal; // 期刊或会议名称


        // Getter 方法用于获取属性值
        // Setter 方法用于设置属性值

        /**
         * 设置论文ID
         * 用uuid生成为id
         * @param id 论文ID
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * 设置论文类型
         * @param type 论文类型（期刊论文、会议论文等）
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * 设置论文DOI号
         * @param doi 论文DOI号
         */
        public void setDoi(String doi) {
            this.doi = doi;
        }

        /**
         * 设置论文标题
         * @param title 论文标题
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * 设置论文作者
         * @param authors 论文作者
         */
        public void setAuthors(String authors) {
            this.authors = authors;
        }

        /**
         * 设置论文发表年份
         * @param year 论文发表年份
         */
        public void setYear(String year) {
            this.year = year;
        }

        /**
         * 设置期刊或会议名称
         * @param journal 期刊或会议名称
         */
        public void setJournal(String journal) {
            this.journal = journal;
        }
        
        /**
         * 获取论文ID
         * @return 论文ID
         */
        public String getId() {
            return id;
        }

        /**
         * 获取论文类型
         * @return 论文类型（期刊论文、会议论文等）
         */
        public String getType() {
            return type;
        }

        /**
         * 获取论文DOI号
         * @return 论文DOI号
         */
        public String getDoi() {
            return doi;
        }

        /**
         * 获取论文标题
         * @return 论文标题
         */
        public String getTitle() {
            return title;
        }

        /**
         * 获取论文作者
         * @return 论文作者
         */
        public String getAuthors() {
            return authors;
        }

        /**
         * 获取论文发表年份
         * @return 论文发表年份
         */
        public String getYear() {
            return year;
        }

        /**
         * 获取期刊或会议名称
         * @return 期刊或会议名称
         */
        public String getJournal() {
            return journal;
        }
    }
}


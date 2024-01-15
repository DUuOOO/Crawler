# Crawler
# tag: crawler, Paper, java, web, jsoup
# https://dblp.org/
这是用java语言实现的、爬取论文网站论文的爬虫项目。
项目实现的功能：
1.可自选搜索关键词爬取
2.采用jsoup进行网页结构解析
3.通过GET请求抓包，发送翻页查询请求
4.抓取的数据存储在mysql数据库，使用jdbc连接，其中id使用uuid作为唯一标识符

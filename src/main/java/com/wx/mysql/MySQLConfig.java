package com.wx.mysql;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * @version 1.0
 * @author 王兴
 * @date 2022/10/2 1:35
 * MySQL表结构导出
 */
public class MySQLConfig {

	public void screw(){
		DataSource dataSource = getDataSource();
// 2.获取数据库文档生成配置（文件路径、文件类型）
		EngineConfig engineConfig = getEngineConfig();
// 3.获取数据库表的处理配置，可忽略
		ProcessConfig processConfig = getProcessConfig();
// 4.Screw 完整配置
		Configuration config = getScrewConfig(dataSource, engineConfig, processConfig);
// 5.执行生成数据库文档
		new DocumentationExecute(config).execute();
	}

	public static void main(String[] args) {
		MySQLConfig screwConfig = new MySQLConfig();
		screwConfig.screw();
	}

	/**
	 * 获取文件生成配置
	 */
	private static EngineConfig getEngineConfig() {
		//生成配置
		return EngineConfig.builder()
				//生成文件路径
				.fileOutputDir("D:/Documents/代码示例/screw-demo/doc")
				//打开目录
				.openOutputDir(true)
				//文件类型
				.fileType(EngineFileType.HTML)
				//生成模板实现
				.produceType(EngineTemplateType.freemarker)
				//自定义文件名称
				.fileName("数据库结构文档").build();
	}

	private static ProcessConfig getProcessConfig() {
		ArrayList<String> ignoreTableName = new ArrayList<>();
		ignoreTableName.add("test_user");
		ignoreTableName.add("test_group");
		ArrayList<String> ignorePrefix = new ArrayList<>();
		ignorePrefix.add("test_");
		ArrayList<String> ignoreSuffix = new ArrayList<>();
		ignoreSuffix.add("_test");
		return ProcessConfig.builder()
				//忽略表名
				.ignoreTableName(ignoreTableName)
				//忽略表前缀
				.ignoreTablePrefix(ignorePrefix)
				//忽略表后缀
				.ignoreTableSuffix(ignoreSuffix)
				.build();
	}

	/**
	 * 获取数据库源
	 */
	private static DataSource getDataSource() {
		//数据源
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/novel?useUnicode=true&characterEncoding=utf8&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai\n");
		hikariConfig.setUsername("root");
		hikariConfig.setPassword("root");
		//设置可以获取tables remarks信息
		hikariConfig.addDataSourceProperty("useInformationSchema", "true");
		hikariConfig.setMinimumIdle(2);
		hikariConfig.setMaximumPoolSize(5);
		return new HikariDataSource(hikariConfig);
	}

	private static Configuration getScrewConfig(DataSource dataSource, EngineConfig engineConfig, ProcessConfig processConfig) {
		return Configuration.builder()
				//版本
				.version("1.0.0")
				//描述
				.description("数据库设计文档生成")
				//数据源
				.dataSource(dataSource)
				//生成配置
				.engineConfig(engineConfig)
				//生成配置
				.produceConfig(processConfig)
				.build();
	}

}

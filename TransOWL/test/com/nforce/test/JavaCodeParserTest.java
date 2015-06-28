package com.nforce.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.nforce.model.StatisticsBean;
import com.nforce.service.FileVisitor;
import com.nforce.service.JavaCodeParser;

public class JavaCodeParserTest {

	StatisticsBean statsBean;

	@Before
	public void setUp() {
		statsBean = StatisticsBean.getInstance();
	}

	@Test
	public void testParser() {
		FileVisitor visitor = new FileVisitor(new File("/mnt/Storage/KTU/Magistrinis/workspace/TransOWL/"), "*.java", 10);
		JavaCodeParser parser = new JavaCodeParser(visitor, new File(""), "/result.owl", statsBean);
		parser.parse();
	}

	@Test
	public void testZhoskParser() {
		FileVisitor visitor = new FileVisitor(new File("/mnt/Storage/Work/mtepis/"), "*.java", 20);
		JavaCodeParser parser = new JavaCodeParser(visitor, new File(""), "/result_zhosk.owl", statsBean);
		parser.parse();
	}

	@Test
	public void testLiferayParser() {
		FileVisitor visitor = new FileVisitor(new File("/mnt/Storage/Work/portal-master/"), "*.java", 20);
		JavaCodeParser parser = new JavaCodeParser(visitor, new File(""), "/result_liferay.owl", statsBean);
		parser.parse();
	}

}

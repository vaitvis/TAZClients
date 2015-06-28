package com.ernesta.test;

import java.io.File;

import org.junit.Test;

import com.ernesta.service.OntologyMerger;

public class OntologyMergerTest {

	@Test
	public void testMerge() {
		File file = new File("/mnt/Storage/KTU/Magistrinis/semantics_dump_unmerged.ttl");
		OntologyMerger merger = new OntologyMerger(file);
		merger.merge();
	}
}

package com.nforce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.nforce.model.FileVisitorCallback;
import com.nforce.model.ScroDefinitions;
import com.nforce.model.StatisticsBean;

public class JavaCodeParser {

	final private StatisticsBean statBean;
	final private FileVisitor visitor;
	final private File resultDir;
	private String optionalFileName;
	private OWLOntologyManager manager;

	public JavaCodeParser(FileVisitor visitor, File resultDir, StatisticsBean statBean) {
		this.visitor = visitor;
		this.resultDir = resultDir;
		this.statBean = statBean;
	}

	public JavaCodeParser(FileVisitor visitor, File resultDir, String optionalFileName, StatisticsBean statBean) {
		this.visitor = visitor;
		this.resultDir = resultDir;
		this.optionalFileName = optionalFileName;
		this.statBean = statBean;
	}

	private OWLOntology createOntology() throws OWLOntologyCreationException {
		manager = OWLManager.createOWLOntologyManager();
		manager.getIRIMappers().add(new SimpleIRIMapper(ScroDefinitions.SCRO, IRI.create(ClassLoader.getSystemResource("schema/scro.owl"))));
		manager.loadOntology(ScroDefinitions.SCRO);
		OWLOntology ontology = manager.createOntology(IRI.create(new File(resultDir.getAbsolutePath()
				+ (StringUtils.isNotBlank(optionalFileName) ? optionalFileName : "/result.owl"))));

		OWLImportsDeclaration importDeclaraton = manager.getOWLDataFactory().getOWLImportsDeclaration(ScroDefinitions.SCRO);
		manager.applyChange(new AddImport(ontology, importDeclaraton));

		return ontology;
	}

	public void parse() {
		try {
			final OWLOntology ontology = createOntology();
			visitor.runVisitor(new FileVisitorCallback() {
				@Override
				public void visit(File file) {
					try {
						String content = new String(Files.readAllBytes(file.toPath()));
						statBean.addFile(file.getAbsolutePath(), content);
						parseFile(content, ontology, file.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			ontology.saveOntology();
		} catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		statBean.printSummary();
		statBean.clear();
	}

	private void parseFile(String content, OWLOntology ontology, String fileName) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		CompilationUnitHandler cuHandler = new CompilationUnitHandler(manager, ontology);
		cuHandler.createCompilationUnit(cu, fileName);

		cu.accept(new ASTVisitor() {

			private AbstractTypeDeclaration tempParentClass;
			private AbstractTypeDeclaration currentClass;

			@Override
			public boolean visit(PackageDeclaration node) {
				PackageHandler handler = new PackageHandler(manager, ontology);
				handler.createPackageIfNeeded(node);
				return super.visit(node);
			}

			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.isMemberTypeDeclaration() || node.isLocalTypeDeclaration()) {
					tempParentClass = currentClass;
				}
				currentClass = node;
				ClassHandler handler = new ClassHandler(manager, ontology);
				handler.createClass(node, cu.getPackage());
				return super.visit(node);
			}

			@Override
			public void endVisit(TypeDeclaration node) {
				if (node.isMemberTypeDeclaration() || node.isLocalTypeDeclaration()) {
					currentClass = tempParentClass;
					tempParentClass = null;
				} else {
					currentClass = null;
				}
				super.endVisit(node);
			}

			@Override
			public boolean visit(MethodDeclaration node) {
				MethodHandler handler = new MethodHandler(manager, ontology);
				handler.createMethod(node, currentClass, cu);
				return super.visit(node);
			}

			@Override
			public boolean visit(EnumDeclaration node) {
				currentClass = node;
				EnumHandler handler = new EnumHandler(manager, ontology);
				handler.createClass(node, cu.getPackage());
				return super.visit(node);
			}

			@Override
			public void endVisit(EnumDeclaration node) {
				currentClass = null;
				super.endVisit(node);
			}
		});
	}

}

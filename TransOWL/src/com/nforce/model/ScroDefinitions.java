package com.nforce.model;

import org.semanticweb.owlapi.model.IRI;

public class ScroDefinitions {

	// Scro
	public static final IRI SCRO = IRI.create("http://www.cs.uwm.edu/~alnusair/ontologies/scro.owl");

	// Package
	public static final IRI PACKAGE = IRI.create(SCRO + "#Package");

	// Compilation Unit
	public static final IRI COMPILATION_UNIT = IRI.create(SCRO + "#CompilationUnit");
	// props
	public static final IRI BELONGS_TO_PACKAGE = IRI.create(ScroDefinitions.SCRO + "#BelongsToPackage");
	//

	// Access Control
	public static final IRI PUBLIC = IRI.create(SCRO + "#PublicModifier");
	public static final IRI PROTECTED = IRI.create(SCRO + "#ProtectedModifier");
	public static final IRI PRIVATE = IRI.create(SCRO + "#PrivateModifier");
	public static final IRI DEFAULT = IRI.create(SCRO + "#Package-Private");
	// props
	public static final IRI HAS_ACCESS_CONTROL = IRI.create(SCRO + "#hasAccessControl");
	//

	// Class
	public static final IRI INTERFACE_TYPE = IRI.create(SCRO + "#InterfaceType");
	public static final IRI ABSTRACT_CLASS = IRI.create(SCRO + "#AbstractClass");
	public static final IRI CONCRETE_CLASS = IRI.create(SCRO + "#ConcreteClass");
	// props
	public static final IRI IS_PACKAGE_MEMBER_OF = IRI.create(ScroDefinitions.SCRO + "#isPackageMemberOf");
	//

	// Enum
	public static final IRI ENUM_TYPE = IRI.create(SCRO + "#EnumType");
	public static final IRI STATIC_MEMBER_ENUM = IRI.create(SCRO + "#StaticMemberEnum");

	// Method
	public static final IRI CLASS_CONSTRUCTOR = IRI.create(SCRO + "#Constructor");
	public static final IRI ABSTRACT_METHOD = IRI.create(SCRO + "#AbstractMethod");
	public static final IRI STATIC_METHOD = IRI.create(SCRO + "#StaticMethod");
	public static final IRI FINAL_METHOD = IRI.create(SCRO + "#FinalMethod");
	public static final IRI INSTANCE_METHOD = IRI.create(SCRO + "#InstanceMethod");
	// props
	public static final IRI IS_CONSTRUCTOR_OF = IRI.create(SCRO + "#isConstructorOf");
	public static final IRI IS_ABSTRACT_METHOD_OF = IRI.create(SCRO + "#isAbstractMethodOf");
	public static final IRI IS_STATIC_METHOD_OF = IRI.create(SCRO + "#isStaticMethodOf");
	public static final IRI IS_FINAL_METHOD_OF = IRI.create(SCRO + "#isFinalMethodOf");
	public static final IRI IS_INSTANCE_METHOD_OF = IRI.create(SCRO + "#isInstanceMethodOf");
}

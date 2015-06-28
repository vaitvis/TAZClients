package com.ernesta.model;

public enum ImportsMap {

	AGENTS("http://semantika.lt/ns/Agents", "Agents_schema.rdf"),
	AGENTS1("http://semantika.lt/ns/Agents1", "Agents_egz1.rdf"),
	ANNOTATIONS("http://semantika.lt/ns/Annotations", "Annotations_schema.rdf"),
	EVENTS("http://semantika.lt/ns/Events", "Events_schema.rdf"),
	EVENTS1("http://semantika.lt/ns/Events1", "Events_egz1.rdf"),
	LOCATIONS("http://semantika.lt/ns/Locations", "Locations_schema.rdf"),
	LOCATIONS1("http://semantika.lt/ns/Locations1", "Locations_egz1.rdf"),
	MOBJECTS("http://semantika.lt/ns/MergedObjects", "MergedObjects_schema.rdf"),
	MUNITS("http://semantika.lt/ns/MeasurementUnits", "MeasurementUnits_schema.rdf"),
	POLITICS("http://semantika.lt/ns/Politics", "Politics_schema.rdf"),
	PRODUCTS("http://semantika.lt/ns/Products", "Products_schema.rdf"),
	SEMLT("http://semantika.lt/ns/SemLT", "SemLT_schema.rdf"),
	TIME("http://semantika.lt/ns/Time", "Time_schema.rdf");

	private String uri;
	private String file;

	private ImportsMap(String uri, String file) {
		this.uri = uri;
		this.file = file;
	}

	public String getUri() {
		return uri;
	}

	public String getFile() {
		return file;
	}

}

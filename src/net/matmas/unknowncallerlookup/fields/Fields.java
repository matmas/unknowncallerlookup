package net.matmas.unknowncallerlookup.fields;

import java.util.LinkedList;
import java.util.List;

public class Fields {
	
	private static List<Field> fields;
	
	public static List<Field> getAll() {
		if (fields == null) {
			fields = new LinkedList<Field>();
			fields.add(new SearchSuffix());
			fields.add(new UrlPrefix());
		}
		return fields;
	}
}

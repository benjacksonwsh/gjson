/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gjson;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a JSON object. A JSON object consists of a set of properties.
 */
public class JSONObject extends JSONValue {

	/*
	 * It is very important that the implementation is based on a linked hash
	 * map. The order of JSON object elements should remain in the same order as
	 * they are encountered. Especially the serializer relies on it.
	 */
	private HashMap<String, JSONValue> map = new LinkedHashMap<String, JSONValue>();

	public JSONObject() {

	}

	/**
	 * Tests whether or not this JSONObject contains the specified property.
	 * 
	 * @param key
	 *            the property to search for
	 * @return <code>true</code> if the JSONObject contains the specified
	 *         property
	 */
	public boolean containsKey(String key) {
		return map.containsKey(key);
	};

	/**
	 * Returns <code>true</code> if <code>other</code> is a {@link JSONObject}
	 * wrapping the same underlying object.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof JSONObject)) {
			return false;
		}
		return map.equals(((JSONObject) other).map);
	}

	/**
	 * Gets the JSONValue associated with the specified property.
	 * 
	 * @param key
	 *            the property to access
	 * @return the value of the specified property, or <code>null</code> if the
	 *         property does not exist
	 * @throws NullPointerException
	 *             if key is <code>null</code>
	 */
	public JSONValue get(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return map.get(key);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	/**
	 * Returns <code>this</code>, as this is a JSONObject.
	 */
	@Override
	public JSONObject isObject() {
		return this;
	}

	/**
	 * Returns the set of properties defined on this JSONObject. The returned
	 * set is immutable.
	 */
	public Set<String> keySet() {
		final String[] keys = map.keySet().toArray(new String[map.size()]);
		return new AbstractSet<String>() {
			@Override
			public boolean contains(Object o) {
				return (o instanceof String) && containsKey((String) o);
			}

			@Override
			public Iterator<String> iterator() {
				return Arrays.asList(keys).iterator();
			}

			@Override
			public int size() {
				return keys.length;
			}
		};
	}

	/**
	 * Assign the specified property to the specified value in this JSONObject.
	 * If the property already has an associated value, it is overwritten.
	 * 
	 * @param key
	 *            the property to assign
	 * @param jsonValue
	 *            the value to assign
	 * @return the previous value of the property, or <code>null</code> if the
	 *         property did not exist
	 * @throws NullPointerException
	 *             if key is <code>null</code>
	 */
	public JSONValue put(String key, JSONValue jsonValue) {
		if (key == null) {
			throw new NullPointerException();
		}
		return map.put(key, jsonValue);
	}

	/**
	 * Determines the number of properties on this object.
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Converts a JSONObject into a JSON representation that can be used to
	 * communicate with a JSON service.
	 * 
	 * @return a JSON string representation of this JSONObject instance
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		boolean first = true;
		for (Map.Entry<String, JSONValue> me : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(JSONString.escapeValue(me.getKey()));
			sb.append(":");
			sb.append(me.getValue());
		}
		sb.append("}");
		return sb.toString();
	}

}

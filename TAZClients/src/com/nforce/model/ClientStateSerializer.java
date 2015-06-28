package com.nforce.model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ClientStateSerializer implements JsonSerializer<ClientState>, JsonDeserializer<ClientState> {

	@Override
	public JsonElement serialize(ClientState src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.value);
	}

	@Override
	public ClientState deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Integer value = json.getAsInt();
		for(ClientState c : ClientState.values()) {
			if(new Integer(c.value).equals(value)) {
				return c;
			}
		}
		return null;
	}
}

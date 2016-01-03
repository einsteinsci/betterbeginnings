package net.einsteinsci.betterbeginnings.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BBJsonLoader
{
	private static Gson gson;

	public static void initialize()
	{
		GsonBuilder builder = new GsonBuilder();
		builder.disableInnerClassSerialization();
		builder.setPrettyPrinting();
		builder.serializeNulls();
		gson = builder.create();
	}

	public static String serializeObject(Object obj)
	{
		return gson.toJson(obj);
	}

	public static <T> T deserializeObject(String json, Class<T> type)
	{
		if (json == null || json.equals(""))
		{
			return null;
		}

		return gson.fromJson(json, type);
	}

	public static Gson getGson()
	{
		return gson;
	}
}

package com.dtssAnWeihai.tools;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MyJSONObject extends JSONObject
{
	public MyJSONObject() {
		super();
    }

    public MyJSONObject(Map copyFrom) {
    	super(copyFrom);
    }

    public MyJSONObject(JSONTokener readFrom) throws JSONException {
    	super(readFrom);
    }

    public MyJSONObject(String json) throws JSONException {
    	super(json);
    }

    public MyJSONObject(JSONObject copyFrom, String[] names) throws JSONException {
    	super(copyFrom, names);
    }

	@Override
	public Object get(String name) throws JSONException
	{
		try
		{
			return super.get(name);
		}
		catch (JSONException e) 
		{
			return null;
		}
	}

	@Override
	public String getString(String name) throws JSONException
	{
		try
		{
			return super.getString(name);
		}
		catch (JSONException e) 
		{
			return null;
		}
	}

	@Override
	public JSONArray getJSONArray(String name) throws JSONException
	{
		try
		{
			return super.getJSONArray(name);
		}
		catch (JSONException e) 
		{
			return null;
		}
	}

	@Override
	public JSONObject getJSONObject(String name) throws JSONException
	{
		try
		{
			return super.getJSONObject(name);
		}
		catch (JSONException e) 
		{
			return null;
		}
	}
	
}

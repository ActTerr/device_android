package mac.yk.devicemanagement.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

public class UtilGsonBuilder {
    public static Gson create(){
		GsonBuilder gb=new GsonBuilder();
		gb.registerTypeAdapter(java.util.Date.class , new  UtilDateSerializer()).setDateFormat(DateFormat.LONG);
		gb.registerTypeAdapter(java.util.Date.class , new  UtilDateDeserializer()).setDateFormat(DateFormat.LONG);
		Gson gson=gb.create();
		return gson;
	}
}
package com.alisverisim.yek.listin;

public class Response{
	private JsonMember1 jsonMember1;
	private JsonMember2 jsonMember2;
	private JsonMember3 jsonMember3;

	public void setJsonMember1(JsonMember1 jsonMember1){
		this.jsonMember1 = jsonMember1;
	}

	public JsonMember1 getJsonMember1(){
		return jsonMember1;
	}

	public void setJsonMember2(JsonMember2 jsonMember2){
		this.jsonMember2 = jsonMember2;
	}

	public JsonMember2 getJsonMember2(){
		return jsonMember2;
	}

	public void setJsonMember3(JsonMember3 jsonMember3){
		this.jsonMember3 = jsonMember3;
	}

	public JsonMember3 getJsonMember3(){
		return jsonMember3;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"1 = '" + jsonMember1 + '\'' + 
			",2 = '" + jsonMember2 + '\'' + 
			",3 = '" + jsonMember3 + '\'' + 
			"}";
		}
}

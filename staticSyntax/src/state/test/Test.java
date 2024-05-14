package state.test;

import java.sql.ResultSet;

import state.sql.SQL;
import state.sql.cond.Col;

public class Test {
	
	public static void main(String[] args){
		
		
		System.out.println(new Start().a().b().getWords());
		System.out.println(new Start().a().c().b().b().a().c().a().b().getWords());
		
		System.out.println(new Number().three().zero().two().two().value());
		
		ResultSet rs = new SQL()
		   .select().c("name").and("age").from("person")
		     .join("courses").on(new Col("id").eq().col("courseId"))
		   .where(new Col("name").eq().val(1).and(new Col("age").isNull()))
		   .orderBy("name").c("age").desc().c("person").fetch();
		
	}

}

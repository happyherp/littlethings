<%@page import="de.carlos.simplexFood.Formats"%>
<%@page import="de.carlos.simplexFood.CostByNutrient.AltSzenario"%>
<%@page import="de.carlos.simplexLife.Alternative"%>
<%@page import="de.carlos.simplexFood.CostByNutrient"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="de.carlos.simplexFood.FoodService"%>
<%@page import="de.carlos.simplexFood.NutritionTarget"%>
<%@page import="de.carlos.simplexFood.food.Nutrient"%>
<%@page import="de.carlos.simplexFood.food.Meal"%>
<%@page import="de.carlos.simplexFood.FoodOptimize"%>
<%@page import="java.util.List"%>
<%@page import="de.carlos.simplexFood.food.IFood"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Simplex Food Calculator</title>
</head>
<body>




<%
FoodService service = new FoodService();
NutritionTarget target = service.makeTarget(request);
List<IFood> excluded = service.makeExcluded(request);
List<IFood> ingredients =  service.getFood(excluded);
List<IFood> result =  service.run(target, excluded);
Meal asMeal = new  Meal(result);

%>

<form method="POST">
	<input type="hidden" name="start" value="start">

	<%if (request.getParameter("start") != null) {%>
		<h1>Results</h1>
		
		
		Total Cost: <%=String.format("%1.2f€",asMeal.getPrice())%><br/>
		Total Weight: <%=String.format("%3.1fg",asMeal.getWeight())%><br/>
		
		
		<table>
			<tr><th>Ingredient</th><th>Amount</th><th>Cost</th><th>Aktion</th><th>Purpose<th></tr>
		
			<%
			for (IFood food: result){
				%>
				<tr>
					<td><%=food.getName() %></td>
					<td align="right"><%=String.format("%3.1fg",food.getWeight())%></td>
					<td align="right"><%=Formats.eur(food.getPrice())%></td>	
					<td><input type="submit" name="exclude_<%=food.getName()%>" value="exclude" /></td>
					<td "><%=FoodOptimize.getPercentages(food, asMeal)%></td>	
				<%
			}
			%>
		</table>
	<%} %>

	<% for (IFood food: excluded){ %>
		<input type="hidden" name="exclude_<%=food.getName()%>" value="1" />
	<%}%>
	<%=excluded.isEmpty()?"":("Excluded: "+excluded)%>
		
	

	<h1>Nutrient Target Selection</h1>
	<table>
		<tr><th>Nutrient</th><th>Min</th><th>Max</th></tr>
		<%for (Nutrient n: Nutrient.values()){ %>
			<tr>
				<td><%=n.name()%></td>
				<td><input type="number" name="min_<%=n.name()%>" value="<%=target.get(n).getMin() %>" /></td>
				<td><input type="number" name="max_<%=n.name()%>" value="<%=target.get(n).getMax() %>" /></td>
			</tr>
		<%}%>
	</table>
	<input type="submit" value="start" />


	<%if (request.getParameter("start") != null) {%>
		<h1>Cost of increasing Nutrients by <%=String.format("%2.0f%%",(CostByNutrient.FACTOR-1.0)*100)%></h1>	
		<table>
			<tr><th>Nutrient</th><th>Cost increase</th><th>Total Cost</th></tr>
			<%
			for (Nutrient n: Nutrient.values()){ 
				out.flush();
				AltSzenario alt = CostByNutrient.buildAlt(target, n, ingredients);%>
				
				<tr>
					<td><%=n.name()%></td>
					<td align="right"><%=Formats.eur(alt.price-asMeal.getPrice())%></td>
					<td align="right"><%=Formats.eur(alt.price)%></td>
				</tr>
			<%}%>
		</table>
	<%} %>

</form>



</body>
</html>
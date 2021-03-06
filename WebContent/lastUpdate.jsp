<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.social.UI.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Applying JQuery DataTables plugin in the Java Server application</title>
        <link href="media/dataTables/social_page.css" rel="stylesheet" type="text/css" />
        <link href="media/dataTables/social_table.css" rel="stylesheet" type="text/css" />
        <link href="media/dataTables/social_escalate.css" rel="stylesheet" type="text/css" />
        <link href="media/dataTables/social_table_jui.css" rel="stylesheet" type="text/css" />
        <link href="media/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" media="all" />
        <link href="media/themes/smoothness/jquery-ui-1.7.2.custom.css" rel="stylesheet" type="text/css" media="all" />
        <script src="scripts/jquery.js" type="text/javascript"></script>
        <script src="scripts/jquery.dataTables.min.js" type="text/javascript"></script>
        <script type="text/javascript">
        $(document).ready(function () {
            $("#socialsentiments").dataTable({    
                "sPaginationType": "full_numbers",
                "bJQueryUI": true,
//                 "dom": "Bfrtip",
//                 "buttons": [
//         			'copy', 'excel', 'pdf'
//     					],
                "aoColumnDefs": [
                    { "sClass": "hide_column", "aTargets": [ 10 ] }
                ]    
//                 "aoColumnDefs": [
//                         { "bSearchable": false, "bVisible": false, "aTargets": [ 8 ] },
//                        // { "bVisible": false, "aTargets": [ 3 ] }
//                 	]
     			});
    			
           // var table = $('#esocialsentiments').DataTable();
			// Hide message column
			//table.column( 8 ).visible( false );
		   		//debugger;
		  	$("#socialsentiments ").dataTable().find("tbody").on('click', 'tr', function () {
        		if (this.rowIndex >= 1) {
        			var coloumn = $(this).find('td').eq(10).text();
			       			    						    
  					alert(coloumn);
        		}
		   	});	
		   	
		   	$('<button id="refresh">Refresh</button>').appendTo('div.dataTables_filter');
		   	
//         	$('#socialsentiments').find('tr').click(function(){
        	    
// 			    var coloumn = $(this).find('td').eq(8).text();
			       			    						    
//   				//alert('You clicked ' + coloumn);
// 			});
        });
        </script>
    </head>
    <body id="dt_example">
        <div id="container">
        	<div id="links">
        	    <a href="index.jsp">Client side Refresh</a> | <a href="plugins.jsp">Client side Refresh with additional plugins |</a>
        		<a href="index.html">Server side Refresh</a> | Client side last update
        		<br/>
        	</div>
            <div id="demo_jui">
		        <table id="socialsentiments" class="display">
		            <thead>
		            	<tr>
		                    <th>Content</th>
		                    <th>FromWho</th>
		                    <th>Kind</th>
		                    <th>ReceivedTime</th>
		                    <th>Subject</th>
		                    <th>ToMe</th>
		                    <th>Sentiment</th>
		                </tr>
		            </thead>
		            <tbody>
		          		<% for(MySocialContent c: MySocialContentRepository.GetMySocialContents()){ %>
						  <tr>
						    <td><%=c.getContent()%></td>
						    <td><%=c.getFromWho()%></td>
						    <td><%=c.getKind()%></td>
						    <td><%=c.getReceivedTime()%></td>
						    <td><%=c.getSubject()%></td>
						    <td><%=c.getToMe()%></td>
						    <td><%=c.getSentiment()%></td>
						  </tr>
						<% } %>
		            </tbody>
		        </table>
		    </div>
        </div>
    </body>
</html>

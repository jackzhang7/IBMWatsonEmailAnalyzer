<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.social.UI.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Applying JQuery DataTables plugin in the Java Server application</title>
        <link href="media/dataTables/social_page.css" rel="stylesheet" type="text/css" />
        <link href="media/dataTables/social_table.css" rel="stylesheet" type="text/css" />
        <link href="media/dataTables/social_table_jui.css" rel="stylesheet" type="text/css" />
        <link href="media/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" media="all" />
        <link href="media/themes/smoothness/jquery-ui-1.7.2.custom.css" rel="stylesheet" type="text/css" media="all" />
        <link href="media/dataTables/ColumnFilterWidgets.css" rel="stylesheet" type="text/css" />
        <script src="scripts/jquery.js" type="text/javascript"></script>
        <script src="scripts/jquery.dataTables.min.js" type="text/javascript"></script>
        <script src="scripts/ColumnFilterWidgets.js" type="text/javascript"></script>
        <script src="scripts/jquery.dataTables.rowGrouping.js" type="text/javascript"></script>
        <script type="text/javascript">
        $(document).ready(function () {
            $("#socialsentiments").dataTable({
                "sPaginationType": "full_numbers",
                "bJQueryUI": true,
                "sDom": 'W<"clear">lfrtip'
            })
           .rowGrouping({sGroupBy: "name", bHideGroupingColumn: false})
		});
		
 
    // Order by the grouping
    $('#example tbody').on( 'click', 'tr.group', function () {
        var currentOrder = table.order()[0];
        if ( currentOrder[0] === 2 && currentOrder[1] === 'asc' ) {
            table.order( [ 2, 'desc' ] ).draw();
        }
        else {
            table.order( [ 2, 'asc' ] ).draw();
        }
    } );
        </script>
    </head>
    <body id="dt_example">
        <div id="container">
        	<div id="links">
        	    <a href="index.jsp">Client side Refresh</a> | Client side Refresh with additional plugins |
        		<a href="index.html">Server side Refresh </a> | <a href="lastUpdate.jsp">Client side last update</a>
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
		            <tfoot>
		                <tr>
		                    <th>Content</th>
		                    <th>FromWho</th>
		                    <th>Kind</th>
		                    <th>ReceivedTime</th>
		                    <th>Subject</th>
		                    <th>ToMe</th>
		                    <th>Sentiment</th>
		                </tr>
		            </tfoot>
		        </table>
		    </div>
        </div>
    </body>
</html>

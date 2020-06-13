		<!-- Header -->
				<%@include file="header.jsp" %>
		<!-- /.Header -->

		<!-- Main Sidebar Container -->
			<%@include file="sidebar.jsp" %>
		<!-- /.sidebar -->

		 <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Add New Invoice</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="billing">Billing</a></li>
              <li class="breadcrumb-item active">Add Invoice</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-12">
          <div class="card">
          <div class="card-header">
        		<i class='fas fa-edit' aria-hidden='true'></i> Iuran IPL Invoice Form
          </div>
            <!-- /.card-header -->
            <div class="card-body">
            <form id="frm-invoice" action="submitInvoice" method="POST">
        		<label>Invoice Item and amount</label>
   			 <div class="form-group" id="dynamic_form">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" placeholder="Enter Item Name" class="form-control">
	                </div>
	                <div class="col-md-3">
	                    <input type="number" class="form-control" name="amount" placeholder="Enter Amount">
	                </div>
	                <div class="button-group">
	                    <a href="javascript:void(0)" class="btn btn-primary" id="plus5">Add More</a>
	                    <a href="javascript:void(0)" class="btn btn-danger" id="minus5">Remove</a>
	                </div>
	            </div>
          	  </div>
        		<label>Add Members</label>
   			   <div class="form-group">
               <table id="memberTable" class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th><i class='fas fa-edit' aria-hidden='true'></i> </th>
            		  <th>Username</th>
                  <th>Name</th>
                </tr>
                </thead>
              </table>
                </div>
                <div>&nbsp;</div>
   				<hr />
  			   <div class="card-footer" align="right">
  			     <input type="hidden" name="billing" id="billing" value="6" />
	             <button type="submit" class="btn btn-info">Submit</button>
                 </form>
               </div>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->                    
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
		<!-- footer -->
			<%@include file="footer.jsp" %>
		<!-- /.footer -->

<script>
        $(document).ready(function() {
        	var dynamic_form =  $("#dynamic_form").dynamicForm("#dynamic_form","#plus5", "#minus5", {
		        limit:4,
		        formPrefix : "dynamic_form",
		        normalizeFullForm : false
		    });

       
		    $("#dynamic_form #minus5").on('click', function(){
		    	var initDynamicId = $(this).closest('#dynamic_form').parent().find("[id^='dynamic_form']").length;
		    	if (initDynamicId === 2) {
		    		$(this).closest('#dynamic_form').next().find('#minus5').hide();
		    	}
		    	$(this).closest('#dynamic_form').remove();
		    });

		
        });
</script>
    		
<script type="text/javascript">
	$(document).ready(function (){
      var table = $("#memberTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "lengthChange" : false,
       				 "bSort" : false,
       			     "ajax" : {
       					 "url" : "memberBillingData?billingID=${billingID}"
 		              },
 		              "columnDefs": [
         			{
        				    "targets": 0,
        				    "checkboxes": {
        			       	"selectRow": true,
        			       	"selectAll": false
      			      }
         			}
     				 ],
  					    "select": {
  				       	"style": "multi"
   					},
 			     	 "columns" : [{
								"data" : "id"
							}, {
								"data" : "username"
							}, {
								"data" : "name"
							}]
					}); 
					
   // Handle form submission event
   $('#frm-invoice').on('submit', function(e){
      var form = this;

      var rows_selected = table.column(0).checkboxes.selected();

      // Iterate over all selected checkboxes
      $.each(rows_selected, function(index, rowId){
         // Create a hidden element
         $(form).append(
             $('<input>')
                .attr('type', 'hidden')
                .attr('name', 'member')
                .val(rowId)
         );
      });
   });
  });
</script>
    
<c:if test="${not empty fn:trim(notification)}">	
	<script type="text/javascript">
	$(function(){
 	 new PNotify({
        title: '${title}',
        text: '${message}',
        type: '${notification}',
        styling: 'bootstrap3'
        });
	});
	</script>
</c:if>
</body>
</html>
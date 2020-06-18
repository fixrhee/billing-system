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
        		 <h3><i class='fas fa-edit' aria-hidden='true'></i>${billingName}</h3> Invoice Form
          </div>
            <!-- /.card-header -->
            <div class="card-body">
            <form id="frm-invoice" action="submitInvoice" method="POST">
        		<label>Invoice Item and Amount</label>
   			 <div class="form-group">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" placeholder="Enter Item Name 1" required value="${item1}" class="form-control">
	                </div>
	                <div>&nbsp;</div>
	                <div class="col-md-3">
	                    <input type="number" class="form-control" name="amount" onblur="findTotal();" required value="${amount1}" placeholder="Enter Amount 1">
	                </div>
	            </div>
          	  </div>
          	   <div class="form-group">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" placeholder="Enter Item Name 2" value="${item2}" class="form-control">
	                </div>
	                <div>&nbsp;</div>
	                <div class="col-md-3">
	                    <input type="number" class="form-control" name="amount" onblur="findTotal();" placeholder="Enter Amount 2" value="${amount2}">
	                </div>
	            </div>
          	  </div>
          	  <div class="form-group">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" placeholder="Enter Item Name 3" value="${item3}" class="form-control">
	                </div>
	                	<div>&nbsp;</div>
	                <div class="col-md-3">
	                    <input type="number" class="form-control" name="amount" onblur="findTotal();" value="${amount3}" placeholder="Enter Amount 3">
	                </div>
	            </div>
          	  </div>
          	  <div class="form-group">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" placeholder="Enter Item Name 4" value="${item4}" class="form-control">
	                </div>
	                	<div>&nbsp;</div>
	                <div class="col-md-3">
	                    <input type="number" class="form-control" name="amount" onblur="findTotal();" value="${amount4}" placeholder="Enter Amount 4">
	                </div>
	            </div>
          	  </div>
          	     <div class="form-group">
                   <div class="custom-control custom-switch">
                      <input type="checkbox" name="saveItem" class="custom-control-input" id="customSwitch1">
                      <label class="custom-control-label" for="customSwitch1">Save Item</label>
                    </div>
                  </div>
            
          	  <div class="form-group">
                <div class="row">
	                <div class="col-md-6">
	                    <font color="red"><label>Total Amount </label></font>
	                </div>
	                <div class="col-md-3">
	            		  <b>Rp. <span id="sum">${totalAmount}</span>,-</b>
	                </div>
	            </div>
          	  </div>
          	  <div>&nbsp;</div>
          	     <hr/>
        		<label>Add Members</label>
   			   <div class="form-group">
             <table id="example" class="table table-bordered table-striped responsive nowrap" cellspacing="0" width="100%">
  			 <thead>
   			   <tr>
		         <th><i class='fas fa-check-square' aria-hidden='true'></i></th>
		         <th>Username</th>
 		         <th>Name</th>
 		         </tr>
   			</thead>
   			</table>
                </div>
                <div>&nbsp;</div>
            </div>
            <!-- /.card-body -->
   				<hr />
  			   <div class="card-footer" align="right">
  			     <input type="hidden" name="billing" id="billing" value="${billingID}" />
	             <button type="submit" class="btn btn-info">Submit</button>
                 </form>
               </div>
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

<script type="text/javascript">
 function findTotal(){
    var arr = document.getElementsByName('amount');
    var tot=0;
    for(var i=0;i<arr.length;i++){
        if(parseFloat(arr[i].value))
            tot += parseFloat(arr[i].value);
    }
    $("#sum").html(tot.toString().replace(/\B(?=(\d{3})+(?!\d))/g, "."));
}
</script>  		
    		
<script type="text/javascript">
	$(document).ready(function() {
   var table = $('#example').DataTable({
   	  'processing' : true,
      'serverSide' : true,
      'lengthChange' : true,
      'bSort' : false,
      'ajax': 'memberBillingData?billingID=${billingID}',
      'columnDefs': [
         {
            'targets': 0,
            'render': function(data, type, row, meta){
               data = '<input type="checkbox" class="dt-checkboxes">'
               if(row[3] === 'UNAVAILABLE'){
                  data = '';
               }
               
               return data;
            },
            'createdCell':  function (td, cellData, rowData, row, col){
               if(rowData[3] === 'UNAVAILABLE'){
                  this.api().cell(td).checkboxes.disable();
               }
            },            
            'checkboxes': {
               'selectRow': true,
               'selectAll': false
               
            }
         },
      ],
      'select': {
         'style': 'multi'
      },      
      'order': [[1, 'asc']]
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

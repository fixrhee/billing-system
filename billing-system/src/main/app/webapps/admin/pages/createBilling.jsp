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
            <h1>Add New Billing</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="billing">Billing</a></li>
              <li class="breadcrumb-item active">Add Billing</li>
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
        		 <h3 class="card-title"><i class='fas fa-edit' aria-hidden='true'></i> Billing Form</h3>       
          </div>
            <!-- /.card-header -->
            <div class="card-body">
            <form id="frm-billing" action="submitBilling" method="POST">
        	         <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="Enter Billing Name">
                  </div>
        	         <div class="form-group">
                    <label for="description">Description</label>
                    <input type="text" class="form-control" id="description" name="description" placeholder="Enter Billing Description">
                  </div>
                  <div class="col-sm-6">
             	  	<div class="form-group">
                        <label>Billing Cycle</label>
                        <select class="form-control" name="cycle" id="cycle">
                          <option value="1">1</option>
                          <option value="2">2</option>
                          <option value="3">3</option>
                          <option value="4">4</option>
                          <option value="5">5</option>
                          <option value="6">6</option>
                          <option value="7">7</option>
                          <option value="8">8</option>
                          <option value="9">9</option>
                          <option value="10">10</option>
                          <option value="12">11</option>
                          <option value="12">12</option>
                          <option value="13">13</option>
                          <option value="14">14</option>
                          <option value="15">15</option>
                          <option value="16">16</option>
                          <option value="17">17</option>
                          <option value="18">18</option>
                          <option value="19">19</option>
                          <option value="20">20</option>
                          <option value="21">21</option>
                          <option value="22">22</option>
                          <option value="23">23</option>
                          <option value="24">24</option>
                          <option value="25">25</option>
                          <option value="26">26</option>
                          <option value="27">27</option>
                          <option value="28">28</option>
                        </select>
                 	</div>
                </div>
                
                 <div class="form-group">
                    <div class="custom-control custom-switch">
                      <input type="checkbox" class="custom-control-input" name="outstanding" id="customSwitch1">
                      <label class="custom-control-label" for="customSwitch1">Enable Outstanding</label>
                    </div>
                  </div>
            </div>
            <!-- /.card-body -->
   			<hr />
  			   <div class="card-footer" align="right">
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

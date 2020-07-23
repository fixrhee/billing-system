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
            <h1>Billing Detail</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="billing">Billing</a></li>
              <li class="breadcrumb-item active">Detail</li>
            </ol>
          </div>
        </div>  
      </div><!-- /.container-fluid -->
      <div align="right">
        		<button type="button" class="btn btn-primary" onclick="window.location.href='createInvoice?billingID=${billingID}';"><i class="fa fa-plus-circle" aria-hidden="true"></i> Add Invoice</button>
        </div>
    </section>

    <!-- Main content -->
    <section class="content">
      
      <div class="row">
        <div class="col-md-6">
          <div class="card">
            <!-- /.card-header -->
   
            <div class="card-body">
              <h3 class="card-title">Billing</h3>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
      	     <table class="table table-striped">
                    <tr>
                      <td><b>Name</b></td>
                      <td align="right">${billingName}</td>
 				    </tr>
                    <tr>
                      <td><b>Description</b></td>
                      <td align="right">${description}</td>
 				    </tr>
                    <tr>
                      <td><b>Payment Due</b></td>
                      <td align="right">${billingCycle} ${period}</td>
 				    </tr>
                    <tr>
                      <td><b>Outstanding</b></td>
                      <td align="right"><span class='right badge badge-info'>${outstanding}</span></td>
 				    </tr>
 			 </table>
            </div>
            <!-- /.card-body -->           
           </div>        
          <!-- /.card -->   
          
        </div>
        <!-- /.col -->
        
        
        <div class="col-md-6">
          <div class="card">
            <!-- /.card-header -->
   
            <div class="card-body">
              <h3 class="card-title">Payment</h3>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
      	     <table class="table table-striped">
                    <tr>
                      <td><b>Periode</b></td>
                      <td align="right">${period}</td>
 				    </tr>
                    <tr>
                      <td><b>Total Member</b></td>
                      <td align="right">${totalMember}</td>
 				    </tr>
                    <tr>
                      <td><b>Total Paid</b></td>
                      <td align="right">${totalPaid}</td>
 				    </tr>
                    <tr>
                      <td><b>Total Unpaid</b></td>
                      <td align="right">${totalUnpaid}</td>
 				    </tr>
 			 </table>
            </div>
            <!-- /.card-body -->           
           </div>        
          <!-- /.card -->   
          
             </div>
        <!-- /.col -->
        
        </div>
      <!-- /.row --> 
      
       <div class="row">
        <div class="col-md-12">
          <div class="card">
      		 <div class="card-header">
              <h3 class="card-title">Billing Member</h3>
              <div class="card-tools">
                <div class="input-group input-group-sm">
                        <select class="custom-select" id="filter">
                          <option value="0">Show All</option>
                          <option value="1">Filter By PAID</option>
                          <option value="2">Filter By UNPAID</option>
                        </select>
                  <div class="input-group-append">
                    <div class="btn btn-primary" onclick="searchFilter(${billingID});">
                      <i class="fas fa-search"></i>
                    </div>
                  </div>
                </div>
              </div>
              <!-- /.card-tools -->
            </div>
            <!-- /.card-header -->   
            
            <div class="card-body">
               <table id="memberTable" class="table table-bordered table-striped responsive nowrap">
                <thead>
                <tr>
                  <th>Username</th>
                  <th>Name</th>
                  <th>Invoice Number</th>
                  <th>Amount</th>
                  <th>Status</th>
                </tr>
                </thead>
              </table>
                            
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
 		$("#memberTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "bFilter": true,
       				 "bSort" : false,
       			     "ajax" : {
       					 "url" : "billingStatusData",
       					  data: { 
      					  "billingID": ${billingID}, 
     					  "filter": ${filter}
       					  }
 		              },
 			     	 "columns" : [{
								"data" : "username"
							}, {
								"data" : "name"
							}, {
								"data" : "invoice"
							}, {
								"data" : "amount"
							}, {
								"data" : "status"
							}]
					});
</script>

<script>
function createInvoice(id){
	window.location.href='createInvoice?billingID=' + id;
};
</script>
<script>
  function searchFilter(id){
   // Selecting the input element and get its value 
   var inputVal = document.getElementById("filter").value;       
   window.location.href='billingDetail?billingID=' + id +'&filter=' +inputVal;
  }
</script>

</body>
</html>

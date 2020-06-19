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
      	     <table class="table">
                    <tr>
                      <td>Name</td>
                      <td align="right">${billingName}</td>
 				    </tr>
                    <tr>
                      <td>Description</td>
                      <td align="right">${description}</td>
 				    </tr>
                    <tr>
                      <td>Billing Cycle</td>
                      <td align="right">${billingCycle} ${period}</td>
 				    </tr>
                    <tr>
                      <td>Outstanding</td>
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
      	     <table class="table">
                    <tr>
                      <td>Period</td>
                      <td align="right">${period}</td>
 				    </tr>
                    <tr>
                      <td>Total Member</td>
                      <td align="right">${totalMember} member(s)</td>
 				    </tr>
                    <tr>
                      <td>Total Paid</td>
                      <td align="right">${totalPaid} member(s)</td>
 				    </tr>
                    <tr>
                      <td>Total Unpaid</td>
                      <td align="right">${totalUnpaid} member(s)</td>
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
                        <select class="custom-select">
                          <option>Show All</option>
                          <option>Filter By PAID</option>
                          <option>Filter By UNPAID</option>
                        </select>
                  <div class="input-group-append">
                    <div class="btn btn-primary">
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
       					 "url" : "billingStatusData?billingID=${billingID}"
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

</body>
</html>

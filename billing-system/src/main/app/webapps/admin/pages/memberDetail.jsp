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
            <h1>Member Detail</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="member">Member</a></li>
              <li class="breadcrumb-item active">Detail</li>
            </ol>
          </div>
        </div>  
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-3">

            <!-- Profile Image -->
            <div class="card card-primary card-outline">
              <div class="card-body box-profile">
                <div class="text-center">
                  <img class="profile-user-img img-fluid img-circle"
                       src="dist/img/user.jpg"
                       alt="User profile picture">
                </div>

                <h3 class="profile-username text-center">${memberName}</h3>

                <p class="text-muted text-center">${memberUsername}</p>

                <ul class="list-group list-group-unbordered mb-3">
                  <li class="list-group-item">
                    <b>Billing</b> <a class="float-right">1,322</a>
                  </li>
                  <li class="list-group-item">
                    <b>Invoice</b> <a class="float-right">543</a>
                  </li>
                  <li class="list-group-item">
                    <b>Unpaid Billing</b> <a class="float-right"><font color="red">13,287</font></a>
                  </li>
                </ul>

                <a href="#" class="btn btn-primary btn-block"><i class='far fa-edit'></i><b> Edit</b></a>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
        </div>
        <!-- /.col -->
        
          <div class="col-md-9">
            <div class="card">
              <!-- /.card-header -->
              <div class="card-body">
               <strong><i class="fas fa-envelope mr-1"></i> Email</strong>
                <p class="text-muted">
                  ${memberEmail}
                </p>
                <hr>
                <strong><i class="fas fa-map-marker-alt mr-1"></i> Address</strong>
                <p class="text-muted">${memberAddress}</p>
                <hr>
                <strong><i class="fa fa-id-card mr-1"></i> IDCard</strong>
                <p class="text-muted">
                  ${memberIDCard}
                </p>
                <hr>
                <strong><i class="fa fa-mobile mr-1"></i> Mobile</strong>
                <p class="text-muted">${memberMsisdn}</p>
                <hr>
                <strong><i class="fa fa-calendar mr-1"></i> Registered Date</strong>
                <p class="text-muted">${memberMsisdn}</p>
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

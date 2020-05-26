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
            <h1>Outlet</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active">Outlet</li>
            </ol>
          </div>
        </div>
        <div align="right">
        		<button type="button" class="btn btn-primary" onclick="window.location.href='createOutlet';">Create New Outlet</button>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example2" class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th>Name</th>
                  <th>Address</th>
                  <th>Created Date</th>
                  <th>Action</th>
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
 		$("#example2")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       			     "ajax" : {
       					 "url" : "viewOutlet"
 		              },
 			     	 "columns" : [{
								"data" : "name"
							}, {
								"data" : "address"
							}, {
								"data" : "createdDate"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<a href='editOutlet?id=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-arrow-circle-right' aria-hidden='true'></i> Edit</a>";
               					 }	
							}]
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

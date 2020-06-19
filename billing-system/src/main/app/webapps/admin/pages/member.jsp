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
            <h1>Member</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active">Member</li>
            </ol>
          </div>
        </div>
                 <div align="right">
        			<button type="button" class="btn btn-primary" onclick="window.location.href='createMember';"><i class="fa fa-plus-circle" aria-hidden="true"></i> Create New Member</button>
       		 </div>
 
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h3 class="card-title">Member List</h3>
            </div>
        
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example2" class="table table-bordered table-striped responsive nowrap">
                <thead>
                <tr>
            		  <th>Username</th>
                  <th>Name</th>
                  <th>Email</th>
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
       				 "bFilter": true,
       				 "bSort" : false,
     			     "ajax" : {
       					 "url" : "memberData"
   					},
 			     	 "columns" : [{
								"data" : "username"
							}, {
								"data" : "name"
							}, {
								"data" : "email"
							}, {
								"data" : "createdDate"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<button type='button' class='btn btn-default btn-sm checkbox-toggle' data-toggle='tooltip' title='View Detail' /><i class='fa fa-info-circle'></i> " + 
                   					 " <button type='button' class='btn btn-default btn-sm checkbox-toggle' data-toggle='tooltip' title='Edit' /><i class='far fa-edit'></i>";
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

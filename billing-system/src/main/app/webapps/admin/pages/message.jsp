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
            <h1>Message</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Message</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
       
        <div class="col-md-12">
          <div class="card">
            <div class="card-header">
              <h3 class="card-title">Inbox</h3>

              <div class="card-tools">
                <div class="input-group input-group-sm">
                  <input type="text" class="form-control" placeholder="Search Mail">
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
            <div class="card-body p-0">
              <div class="mailbox-controls">
                <div class="btn-group">
                  <button type="button" class="btn btn-default btn-sm"><i class="far fa-trash-alt"></i></button>
                </div>
                <!-- /.btn-group -->
                <button type="button" class="btn btn-default btn-sm"><i class="fas fa-sync-alt"></i></button>
              <div class="table-responsive mailbox-messages">
                <table id="example2" class="table table-hover table-striped responsive nowrap">
                <thead>
                <tr>
                <th></th>
                <th></th>
                <th></th>
        			<th></th>
        			<th></th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                <th></th>
                <th></th>
                <th></th>
        			<th></th>
        			<th></th>
                </tr>                
                </tfoot>
                </table>
                <div>&nbsp;</div>
                <!-- /.table -->
              </div>
              <!-- /.mail-box-messages -->
            </div>
            <!-- /.card-body -->
            <div class="card-footer p-0">
              <div class="mailbox-controls">
                <div class="btn-group">
                  <button type="button" class="btn btn-default btn-sm"><i class="far fa-trash-alt"></i></button>
                </div>
                <!-- /.btn-group -->
                <button type="button" class="btn btn-default btn-sm"><i class="fas fa-sync-alt"></i></button>
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

<script>
 		$("#example2")
				.DataTable({
   	  'processing' : true,
      'serverSide' : true,
      'lengthChange' : false,
      'bSort' : false,
      'bFilter': false, 
      'pagingType' : 'simple',
      'ajax': 'messageData',
      'columnDefs': [
         {
            'targets': 0,
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
</script>	

</body>
</html>

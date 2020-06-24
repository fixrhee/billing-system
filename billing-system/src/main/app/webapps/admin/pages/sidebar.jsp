		<aside class="main-sidebar sidebar-dark-primary elevation-4">
			<!-- Brand Logo -->
			<a href="/admin/index" class="brand-link"> <img
				src="dist/img/AdminLTELogo.png" alt="AdminLTE Logo"
				class="brand-image img-circle elevation-3" style="opacity: .8">
				<span class="brand-text font-weight-light">AdminBilling</span>
			</a>

			<!-- Sidebar -->
			<div class="sidebar">
				<!-- Sidebar user panel (optional) -->
				<div class="user-panel mt-3 pb-3 mb-3 d-flex">
					<div class="image">
						<img src="dist/img/user-160.jpg"
							class="img-circle elevation-2" alt="User Image">
					</div>
					<div class="info">
						<a href="#" class="d-block">${name}</a>
					</div>
				</div>
					<div class="user-panel mt-3 pb-3 mb-3 d-flex">
					<div class="image">
						<font color="red" class="lead"><h3>Rp.</h3></font>
					</div>
					<div class="info">
						<font class="lead" color="#C0C0C0"><b>123.394.000,-</b></font>
					</div>
					
				<!-- div class="info">
						<a href="#" class="lead"><h5><i class="fa fa-minus-circle" aria-hidden="true"></i></h5></a>
					</div -->
					
				</div>
				
				
				<!-- Sidebar Menu -->
				<nav class="mt-2">
					<ul class="nav nav-pills nav-sidebar flex-column"
						data-widget="treeview" role="menu" data-accordion="false">
						<!-- Add icons to the links using the .nav-icon class
          			     with font-awesome or any other icon font library -->
						
						${menu}
					
					</ul>
				</nav>
				<!-- /.sidebar-menu -->
			</div>
			<!-- /.sidebar -->
		</aside>
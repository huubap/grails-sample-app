class UrlMappings {

	static mappings = {
//        "/$controller/$action?/$id?(.$format)?"{
//            constraints {
//                // apply constraints here
//            }
//        }
//
//        "/"(view:"/index")
//        "500"(view:'/error')
    login: "/login"(controller: 'login')
    logout: "/logout"(controller: 'logout')
    callback: "/callback"(controller: 'callback')
    home: "/portal/home"(controller: 'home')

    "/"(view:"/index")
    "500"(view:'/error')
    "404"(view:'/notFound')
	}
}

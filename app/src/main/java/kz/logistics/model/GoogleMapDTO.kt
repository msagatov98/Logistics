package kz.logistics.model

class GoogleMapDTO() {
    val routes = ArrayList<Routes>()
}

class Routes {
    val legs = ArrayList<Legs>()
}

class Legs {
    var distance = Distance()
    var duration = Duration()
    var endAddress = ""
    var startAddress = ""
    var endLocation = Location()
    var startLocation = Location()
    var steps = ArrayList<Steps>()
}

class Steps {
    var distance = Distance()
    var duration = Duration()
    var endAddress = ""
    var startAddress = ""
    var endLocation = Location()
    var startLocation = Location()
    var polyline = Polyline()
    var travelMode = ""
    var maneuver = ""
}

class Duration {
    var text = ""
    var value = 0
}

class Distance {
    var text = ""
    var value = 0
}

class Polyline {
    var points = ""
}

class Location {
    var lat = ""
    var lng = ""
}
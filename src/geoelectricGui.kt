fun geoelectricsGui() {
    if (!"tkrplot" in installedPackages()) {
        throw Exception("Please install the `tkrplot` package first.")
    }
    source(system.file("gui/gui.r", package = "geoelectrics"))
}

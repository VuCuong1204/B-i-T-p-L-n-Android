package baitaplon.babyphoto.screen.listimage

interface IListImage {
    fun setCamera()
    fun setImage(position: Int, select: Boolean)
    fun showPreview(linkFolder: String)
}

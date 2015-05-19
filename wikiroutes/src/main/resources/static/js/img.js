$.validator.addMethod('imagedim', function(value, element, param) {
  var _URL = window.URL;
        var  img;
        if ((element = this.files[0])) {
            img = new Image();
            img.onload = function () {
                console.log("Width:" + this.width + "   Height: " + this.height);//this will give you image width and height and you can easily validate here....

                return this.width >= param
            };
            img.src = _URL.createObjectURL(element);
        }
});
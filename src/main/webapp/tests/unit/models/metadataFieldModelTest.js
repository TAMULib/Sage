describe("model: MetadataField", function () {
  var $rootScope, $scope, WsApi, model;

  var initializeVariables = function (settings) {
    inject(function (_$rootScope_, _WsApi_) {
      $rootScope = _$rootScope_;

      WsApi = _WsApi_;
    });
  };

  var initializeModel = function (settings) {
    inject(function (_MetadataField_) {
      $scope = $rootScope.$new();

      model = angular.extend(new _MetadataField_(), dataMetadataField1);
    });
  };

  beforeEach(function () {
    module("core");
    module("sage");
    module("mock.wsApi");

    initializeVariables();
    initializeModel();
  });

  describe("Is the model", function () {
    it("defined", function () {
      expect(model).toBeDefined();
    });
  });
});

const avro = require('avsc');
const type = avro.Type.forSchema({
  "type" : "record",
  "name" : "DataResponse",
  "namespace" : "name.mrkandreev.avro",
  "fields" : [ {
    "name" : "values",
    "type" : {
      "type" : "array",
      "items" : {
        "type" : "record",
        "name" : "DataResponseValue",
        "fields" : [ {
          "name" : "timestamp",
          "type" : "long"
        }, {
          "name" : "value",
          "type" : "double"
        } ]
      },
      "java-class" : "java.util.List"
    }
  } ]
});

window.avro_decode = (buf) => {
    return type.fromBuffer(new Buffer(buf));
};

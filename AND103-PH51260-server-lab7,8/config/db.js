const mongoose = require("mongoose");

const mongoURI = "mongodb://127.0.0.1:27017/MyDatabase";

// Hàm kết nối
const connect = async () => {
  try {
    await mongoose.connect(mongoURI); 
    console.log("Kết nối MongoDB thành công");
  } catch (error) {
    console.error("Kết nối thất bại: ", error);
  }
};

module.exports = { connect };

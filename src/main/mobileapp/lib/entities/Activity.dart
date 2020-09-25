class Activity {
  String title;
  String startDate;
  String finishDate;
  double latitude;
  double longitude;
  int remainingQuota;

  Activity(this.title, this.startDate, this.finishDate, this.latitude,
      this.longitude, this.remainingQuota);

  Activity.fromJson(Map<String, dynamic> json) {
    title = json['title'];
    startDate = json['startDate'];
    finishDate = json['finishDate'];
    latitude = json['latitude'];
    longitude = json['longitude'];
    remainingQuota = json['remainingQuota'];
  }
}
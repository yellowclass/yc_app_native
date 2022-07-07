class InAppUpdateModel {
  final int updateAvailability;
  final bool immediateUpdateAllowed;
  final bool flexibleUpdateAllowed;
  final int availableVersionCode;
  final int installStatus;
  final String packageName;
  final int updatePriority;
  final int clientVersionStalenessDays;
  InAppUpdateEnum? inAppUpdateEnum;

  InAppUpdateModel({
    this.updateAvailability = -1,
    this.immediateUpdateAllowed = false,
    this.flexibleUpdateAllowed = false,
    this.availableVersionCode = -1,
    this.installStatus = -1,
    this.packageName = "",
    this.clientVersionStalenessDays = -1,
    this.updatePriority = -1,
    this.inAppUpdateEnum,
  });
}

class UpdateAvailability {
  UpdateAvailability._();

  static int get unknown => 0;

  static int get updateNotAvailable => 1;

  static int get updateAvailable => 2;

  static int get developerTriggeredUpdateInProgress => 3;

  static String getString(int updateavailability) {
    switch (updateavailability) {
      case 0:
        return "unknown_state";
      case 1:
        return "update_not_available";
      case 2:
        return "update_available";
      case 3:
        return "developer_triggered_update_in_progress";
      default:
        return "unknown_availablity";
    }
  }
}

enum InAppUpdateEnum {
  NO_UPDATE,
  FLEXIBLE_UPDATE,
  MAJOR_UPDATE,
}

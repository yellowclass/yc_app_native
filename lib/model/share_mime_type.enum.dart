enum YCMimeType { IMAGES, VIDEOS, TEXTS, EMAIL }

extension YCMimeExtension on YCMimeType {
  String shareableMime() {
    switch (this) {
      case YCMimeType.IMAGES:
        return "image/*";
      case YCMimeType.VIDEOS:
        return "video/*";
      case YCMimeType.TEXTS:
        return "text/*";
      case YCMimeType.EMAIL:
        return "message/rfc822";
    }
  }
}

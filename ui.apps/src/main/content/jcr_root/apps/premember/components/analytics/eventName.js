'use strict';
use(function () {
  if (this.text) {
    // /[^\x00-\x7F]/g ==> all non-ascii characters
      return this.text.trim().toLowerCase().replace(/\s/g, '_').replace(/[^\x00-\x7F]/g, '');
  }
  return "";
});
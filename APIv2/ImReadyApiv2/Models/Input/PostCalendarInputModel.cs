﻿using ImReady.Data.Models;
using ImReady.Data.Models.Users;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ImReadyApiv2.Models.Input {
	public class PostCalendarInputModel {
		
		[Required]
		public string Title { get; set; }

		[Required]
		[DataType(DataType.DateTime)]
		public DateTime StartDate { get; set; }

		[DataType(DataType.DateTime)]
		public DateTime EndDate { get; set; }

		public String Location { get; set; }

		public String Remark { get; set; }

		internal Calendar getModel (User user) {
			Calendar calendar = new Calendar();
			calendar.UserId = user.Id;
			calendar.User = user;
			calendar.Title = Title;
			calendar.StartDate = StartDate;
			calendar.EndDate = EndDate;
			calendar.Location = Location;
			calendar.Remark = Remark;

			return calendar;
		}
	}
}
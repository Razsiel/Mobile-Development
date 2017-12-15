﻿using ImReady.Data.Models;
using ImReady.Data.Models.Users;
using ImReady.Service.Services.Interfaces;
using ImReadyApiv2.Models.Input;
using ImReadyApiv2.Results;
using ImReadyApiv2.Services.Interfaces;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Description;

namespace ImReadyApiv2.Controllers {

	/// <summary>
	/// Api controller for Calendar calls
	/// </summary>
	[RoutePrefix("api/user/{userId}/calendar")]
	public class CalendarController : BaseApiController{

		private readonly IUserService _userService;
		private readonly ICalendarService _calendarService;

		/// <summary>
		/// ctor
		/// </summary>
		/// <param name="userService">Service class for user business logic</param>
		/// <param name="calendarService">Service class for user business logic</param>
		public CalendarController (IUserService userService, ICalendarService calendarService) {
			_userService = userService;
			_calendarService = calendarService;
		}

		/// <summary>
		/// Returns all calendar items
		/// </summary>
		/// <remarks>Gets all calendar items for the specified user</remarks>
		/// <response code="200">OK</response>
		/// <response code="404">Not Found</response>
		[ResponseType(typeof(List<CalendarResult>))]
		[Route("")]
		public IHttpActionResult Get(string userId) {
			User user = _userService.GetUser(userId);

			if (user == null) {
				return NotFound();
			}

			List<Calendar> calendarItems = _calendarService.GetCalendarItems(userId);
			List<CalendarResult> calendarResults = calendarItems.Select(ci => new CalendarResult(ci)).ToList();

			return Ok(calendarResults);
		}

		/// <summary>
		/// Create calendar item
		/// </summary>
		/// <remarks>Create new calendar item for the specified user</remarks>
		/// <response code="204">No Content</response>
		/// <response code="404">Not Found</response>
		[Route("")]
		public IHttpActionResult Post (string userId, [FromBody]PostCalendarInputModel model) {
			//get user object
			User user =_userService.GetUser(userId);

			if(user == null) {
				return NotFound();
			}

			Calendar calendar = model.getModel(user);

			Validate(calendar);

			if (!ModelState.IsValid) {
				return BadRequest(ModelState);
			}

			_calendarService.CreateCalendarItem(calendar);

			return StatusCode(System.Net.HttpStatusCode.NoContent);
		}

		/// <summary>
		/// Delete calendar item
		/// </summary>
		/// <remarks>Delete a calendar item for the specified user</remarks>
		/// <response code="204">No Content</response>
		/// <response code="404">Not Found</response>
		[Route("{calendarId}")]
		public IHttpActionResult Delete(string userId, string calendarId) {
			Calendar calendar = _calendarService.getCalendarItem(userId, calendarId);

			if (calendar == null) {
				return NotFound();
			}

			_calendarService.DeleteCalendarItem(calendar);

			return StatusCode(System.Net.HttpStatusCode.NoContent);
		}
	}
}
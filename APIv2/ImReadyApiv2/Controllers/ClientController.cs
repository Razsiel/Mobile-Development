﻿using ImReady.Data.Enums;
using ImReady.Data.Models;
using ImReady.Data.Models.Users;
using ImReady.Service.Services;
using ImReadyApiv2.Context;
using ImReadyApiv2.Models;
using ImReadyApiv2.Models.Input;
using ImReadyApiv2.Results;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;

namespace ImReadyApiv2.Controllers
{
    [RoutePrefix("api/client")]
    public class ClientController : BaseApiController
    {
        private readonly IClientService _clientService;
        private readonly IClientActivityService _clientActivityService;
        private readonly IClientComponentService _clientComponentService;

        public ClientController(IClientService clientService, IClientActivityService clientActivityService, IClientComponentService clientComponentService)
        {
            _clientService = clientService;
            _clientActivityService = clientActivityService;
            _clientComponentService = clientComponentService;
        }

        // GET: api/Client/5
        public async Task<IHttpActionResult> Get(string id)
        {
            var client = _clientService.GetClient(id);
            if (client == null)
            {
                return NotFound();
            }
            var clientResult = new ClientUserResult(client);
            clientResult.Roles = await _userManager.GetRolesAsync(client.Id);
            return Ok(clientResult);
        }

        // POST: api/Client
        public async Task<IHttpActionResult> Post([FromBody]PostUserInputModel model)
        {
            var user = model.GetUser<Client>();

            Validate<User>(user);

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var result = await _userManager.CreateAsync(user, model.Password);
            if (result.Succeeded)
            {
                result = await _userManager.AddToRoleAsync(user.Id, Role.CLIENT.ToString());
                if (result.Succeeded)
                {
                    user = _clientService.GetClient(user.Id);
                    return Ok(user);
                }
            }

            return BadRequest("could not create the user or assign the role");
        }

        /// <summary>
        /// Put to upload content of the clientActivity
        /// </summary>
        /// <param name="id">ClientId</param>
        /// <param name="activityId">ClientActivityId</param>
        /// <param name="value">PostClientActivityInputModel</param>
        // PUT: api/Client/1/activity/2
        [Route("{id}/activity/{activityId}")]
        public void Put(string id, string activityId, [FromBody]PostClientActivityInputModel value)
        {
            ClientActivity activity = _clientActivityService.getById(activityId);

            if (activity.ClientComponent.ClientBuildingBlock.ClientId == id)
            {
                activity.Content = value.Content;
                activity.Status = value.Status;

                if (value.Status != Status.DONE)
                {
                    _clientActivityService.EditActivity(activity);
                }
            }
        }

        [Route("{id}/component/{componentId}")]
        public async Task<IHttpActionResult> Post(string id, string componentId)
        {
            var result = _clientComponentService.Enroll(id, componentId);

            if (result)
            {
                return Ok();
            }
            return BadRequest($"Could not enroll the client {id} at component {componentId}");
        }


        // DELETE: api/Client/5
        public void Delete(int id)
        {
        }
    }
}

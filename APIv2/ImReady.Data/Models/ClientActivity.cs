﻿using ImReady.Data.Enums;
using ImReady.Data.Models.Users;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImReady.Data.Models
{
    [Table("ClientActivity")]
    public class ClientActivity : EntityModel
    {
        public string ClientComponentId { get; set; }

        public string ActivityId { get; set; }

        public Status Status { get; set; }

        public string Content { get; set; }

        public virtual ICollection<Feedback> Feedback { get; set; }

        [ForeignKey("ClientComponentId")]
        public virtual ClientComponent ClientComponent { get; set; }

        [ForeignKey("ActivityId")]
        public virtual Activity Activity { get; set; }
    }
}

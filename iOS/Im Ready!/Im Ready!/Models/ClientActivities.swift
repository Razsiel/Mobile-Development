//
//  ClientActivities.swift
//  Im Ready!
//
//  Created by Wouter Vermeij on 18/12/2017.
//  Copyright © 2017 Wouter Vermeij. All rights reserved.
//

import Foundation

struct ClientActivities : Decodable {
    let name : String?
    let description : String?
    let points : Int?
    let id : String?
    
    enum CodingKeys: String, CodingKey {        
        case name = "Name"
        case description = "Description"
        case points = "Points"
        case id = "Id"
    }
    
    init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        name = try values.decodeIfPresent(String.self, forKey: .name)
        description = try values.decodeIfPresent(String.self, forKey: .description)
        points = try values.decodeIfPresent(Int.self, forKey: .points)
        id = try values.decodeIfPresent(String.self, forKey: .id)
    }

}

package yte.intern.project.manageActivities.mapper;

import org.mapstruct.Mapper;
import yte.intern.project.manageActivities.dto.ActivityDTO;
import yte.intern.project.manageActivities.entity.Activity;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityDTO mapToDto(Activity activity);

    Activity mapToEntity(ActivityDTO activityDTO);

    List<ActivityDTO> mapToDto(List<Activity> activityList);

    List<Activity> mapToEntity(List<ActivityDTO> activityDTOList);


}

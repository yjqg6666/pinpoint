import useSWR from 'swr';
import { END_POINTS } from '@pinpoint-fe/ui/constants';
import { swrConfigs } from './swrConfigs';

export const useGetApplicationList = () => {
  return useSWR(END_POINTS.APPLICATION_LIST, swrConfigs);
};
